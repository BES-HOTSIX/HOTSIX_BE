package com.example.hotsix_be.payment.pay.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.openapi.PayApi;
import com.example.hotsix_be.payment.pay.service.PayService;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayController implements PayApi {
    private final CashLogService cashLogService;
    private final PayService payService;
    private final ReservationService reservationService;
    private final CouponService couponService;

    @GetMapping("/{reserveId}")
    @MemberOnly
    public ResponseEntity<ResponseDto<ReservationDetailResponse>> showPay(
            @PathVariable final Long reserveId,
            @Auth final Accessor accessor
    ) {
        // 이 메소드에서 로그인한 사용자가 예약한 본인인지도 확인
        ReservationDetailResponse reservationDetailResponse = reservationService.getUnpaidDetailById(reserveId,
                accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                HttpStatus.OK.value(),
                "결제창 조회 성공", null,
                null, reservationDetailResponse));
    }

    // 결제창에서 결제하기 버튼을 누를 경우 아래 메소드가 작동
    // 이미 생성되어있는 임시 예약
    @PostMapping("/{reserveId}/byCash")
    @MemberOnly
    public ResponseEntity<ResponseDto<CashLogIdResponse>> payByCash(
            @PathVariable final Long reserveId,
            @RequestBody final UseCouponRequest useCouponRequest,
            @Auth final Accessor accessor
    ) {

        Reservation reservation = reservationService.findUnpaidById(reserveId);

        if (useCouponRequest.getDiscountAmount() > 0) {
            couponService.deleteCoupon(reservation, useCouponRequest);
        } // 쿠폰 사용 시 쿠폰 삭제

        // 이용자 결제
        CashLog cashLog = payService.payByCashOnly(reservation, useCouponRequest);

        CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLog.getId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예치금 결제가 완료되었습니다.", null,
                        null, cashLogIdResponse
                )
        );
    }

    @PostMapping("/{reserveId}/byToss")
    @MemberOnly
    public ResponseEntity<ResponseDto<CashLogIdResponse>> payByToss(
            @RequestBody final TossConfirmRequest tossConfirmRequest,
            @PathVariable final Long reserveId,
            @Auth Accessor accessor
    ) {
        Reservation reservation = reservationService.findUnpaidById(reserveId);

        Long cashLogId = payService.payByTossPayments(tossConfirmRequest, reservation,
                tossConfirmRequest.getDiscountAmount()).getId();

        CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLogId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "토스페이먼츠 결제가 완료되었습니다.", null,
                        null, cashLogIdResponse
                )
        );
    }
}
