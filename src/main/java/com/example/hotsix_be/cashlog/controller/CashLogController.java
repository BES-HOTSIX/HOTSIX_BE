package com.example.hotsix_be.cashlog.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.dto.request.TossConfirmRequest;
import com.example.hotsix_be.cashlog.dto.response.*;
import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.exception.CashException;
import com.example.hotsix_be.cashlog.service.CashLogService;
import com.example.hotsix_be.cashlog.service.TossService;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cashLog")
@Slf4j
public class CashLogController {
    private final CashLogService cashLogService;
    private final ReservationService reservationService;
    private final TossService tossService;

    @GetMapping("/detail/{cashLogId}")
    public ResponseEntity<?> getTestCashLog(@PathVariable(value = "cashLogId") final Long id) {
        CashLog cashLog = cashLogService.findById(id).orElse(null);

        if (cashLog == null) throw new CashException(INVALID_REQUEST);

        System.out.println(id);
        TestResponse cashLogDetailResponse = TestResponse.of(cashLog);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "테스트 캐쉬로그 조회 성공", null,
                null, cashLogDetailResponse));
    }

    @GetMapping("/me") // TODO 예약 내역도 추가
    @MemberOnly
    public ResponseEntity<?> showMyCashLogs(
            @PageableDefault(size = 5) final Pageable pageable,
            @Auth final Accessor accessor
    ) {
        Long memberId = accessor.getMemberId();

        Page<CashLog> cashLogs = cashLogService.findMyPageList(memberId, pageable);

        List<CashLogConfirmResponse> cashLogConfirmResponses =
                cashLogs.stream()
                        .map(CashLogConfirmResponse::of)
                        .toList();

        PageImpl<CashLogConfirmResponse> cashLogConfirmPage =
                new PageImpl<>(
                        cashLogConfirmResponses,
                        pageable,
                        cashLogs.getTotalElements());

        MyCashLogResponse myCashLogResponse = cashLogService.getMyCashLogById(
                memberId,
                cashLogConfirmPage
        );


        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "캐시 사용 내역 조회 성공", null,
                null, myCashLogResponse
        ));
    }

    // TODO 무통장 입금 시 어드민이 수리하는 형식으로 수정할 예정
    @PostMapping("/addCash")
    public ResponseEntity<?> addCash(@RequestBody final AddCashRequest addCashRequest) {

        cashLogService.addCash(addCashRequest, EventType.충전__무통장입금);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "무통장 입금이 완료되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/payByCash/{reserveId}")
    @MemberOnly
    public ResponseEntity<?> showPay(
            @PathVariable(value = "reserveId") final Long reserveId,
            @Auth final Accessor accessor
    ) {
        // 이 메소드에서 로그인한 사용자가 예약한 본인인지도 확인
        ReservationDetailResponse reservationDetailResponse = reservationService.getUnpaidDetailById(reserveId, accessor.getMemberId());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "결제창 조회 성공", null,
                null, reservationDetailResponse));
    }

    // 결제창에서 결제하기 버튼을 누를 경우 아래 메소드가 작동
    // 이미 생성되어있는 임시 예약
    @PostMapping("/payByCash/{reserveId}")
    public ResponseEntity<?> payByCash(@PathVariable(value = "reserveId") final Long reserveId) {
        Reservation reservation = reservationService.findUnpaidById(reserveId).orElseThrow(() -> new CashException(INVALID_REQUEST));

        if (!cashLogService.canPay(reservation, reservation.getPrice())) throw new CashException(INSUFFICIENT_DEPOSIT);

        // 이용자 결제
        CashLog cashLog = cashLogService.payByCashOnly(reservation);

        CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLog.getId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예치금 결제가 완료되었습니다.", null,
                        null, cashLogIdResponse
                )
        );
    }

    @PostMapping("/payByToss/{reserveId}")
    public Mono<ResponseEntity<ResponseDto<?>>> confirmPayment(
            @RequestBody final TossConfirmRequest tossConfirmRequest,
            @PathVariable(value = "reserveId") final Long reserveId
    ) {
        Reservation reservation = reservationService.findUnpaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));
        if (!cashLogService.canPay(reserveId, Long.parseLong(tossConfirmRequest.getAmount()))) throw new CashException(INSUFFICIENT_DEPOSIT);
        Long cashLogId = cashLogService.payByTossPayments(tossConfirmRequest, reservation).getId();

        return tossService.confirmTossPayment(tossConfirmRequest)
                .flatMap(tossPaymentResponse -> {
                    CashLogIdResponse cashLogIdResponse = cashLogService.getCashLogIdById(cashLogId, tossPaymentResponse);
                    return Mono.just(ResponseEntity.ok(
                            new ResponseDto<>(
                                    HttpStatus.OK.value(),
                                    "토스페이먼츠 결제가 완료되었습니다.", null,
                                    null, cashLogIdResponse)
                    ));
                });
    }

    // TODO cashLog 의 내용에 따라 다르게 쓰이는 범용 완료 페이지로 만들 예정
    @GetMapping("/{cashLogId}/confirm")
    @MemberOnly
    public ResponseEntity<?> showConfirm(
            @PathVariable(value = "cashLogId") final Long cashLogId,
            @Auth Accessor accessor
    ) {
        CashLog cashLog = cashLogService.findById(cashLogId).orElseThrow(() -> new CashException(NOT_FOUND_CASHLOG_ID));
        if (!cashLog.getMember().getId().equals(accessor.getMemberId())) throw new CashException(INVALID_AUTHORITY);
        ConfirmResponse confirmResponse = cashLogService.getConfirmRespById(cashLogId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "%s이(가) 완료되었습니다.".formatted(confirmResponse.getEventType()), null,
                        null, confirmResponse
                )
        );
    }

    @PatchMapping("/{reserveId}/cancel")
    @MemberOnly
    public ResponseEntity<?> cancelReservation(
            @PathVariable(value = "reserveId") final Long reserveId,
            @Auth Accessor accessor
    ) {
        // 조회
        Reservation reservation = reservationService.findPaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        if (!reservation.getMember().getId().equals(accessor.getMemberId())) throw new ReservationException(INVALID_AUTHORITY);

        if (!reservation.isCancelable()) throw new ReservationException(CANCELLATION_PERIOD_EXPIRED);

        CashLog cashLog = cashLogService.cancelReservation(reservation);

        CashLogIdResponse cashLogIdResponse = CashLogIdResponse.of(cashLog.getId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 취소가 완료되었습니다.", null,
                        null, cashLogIdResponse
                ));
    }
}
