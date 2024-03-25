package com.example.hotsix_be.payment.cashlog.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.openapi.CashLogApi;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_AUTHORITY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cashLog")
@Slf4j
public class CashLogController implements CashLogApi {
    private final CashLogService cashLogService;

    @GetMapping("/me")
    @MemberOnly
    public ResponseEntity<ResponseDto<MyCashLogResponse>> showMyCashLogs(
            final Pageable pageable,
            @Auth final Accessor accessor
    ) {
        MyCashLogResponse myCashLogResponse = cashLogService.findMyPageList(accessor.getMemberId(), pageable);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "캐시 사용 내역 조회 성공", null,
                null, myCashLogResponse
        ));
    }

    @GetMapping("/{cashLogId}/confirm")
    @MemberOnly
    public ResponseEntity<ResponseDto<ConfirmResponse>> showConfirm(
            @PathVariable final Long cashLogId,
            @Auth final Accessor accessor
    ) {
        CashLog cashLog = cashLogService.findById(cashLogId);
        if (!cashLog.getMember().getId().equals(accessor.getMemberId())) throw new PaymentException(INVALID_AUTHORITY);
        ConfirmResponse confirmResponse = cashLogService.getConfirmRespById(cashLogId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "%s이(가) 완료되었습니다.".formatted(confirmResponse.getEventType()), null,
                        null, confirmResponse
                )
        );
    }
}