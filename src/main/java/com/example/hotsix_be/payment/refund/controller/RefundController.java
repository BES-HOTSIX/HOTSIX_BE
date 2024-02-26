package com.example.hotsix_be.payment.refund.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.refund.openapi.RefundApi;
import com.example.hotsix_be.payment.refund.service.RefundService;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/refund")
public class RefundController implements RefundApi {
    private final RefundService refundService;
    private final CashLogService cashLogService;
    private final ReservationService reservationService;

    @PatchMapping("/reserve/{reserveId}")
    @MemberOnly
    public ResponseEntity<ResponseDto<CashLogIdResponse>> cancelReservation(
            @PathVariable final Long reserveId,
            @Auth final Accessor accessor
    ) {
        // 조회
        Reservation reservation = reservationService.findPaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        if (!reservation.getMember().getId().equals(accessor.getMemberId()))
            throw new ReservationException(INVALID_AUTHORITY);

        if (!reservation.isCancelable()) throw new ReservationException(CANCELLATION_PERIOD_EXPIRED);

        CashLog cashLog = refundService.doRefund(reservation);

        CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLog.getId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 취소가 완료되었습니다.", null,
                        null, cashLogIdResponse
                ));
    }
}
