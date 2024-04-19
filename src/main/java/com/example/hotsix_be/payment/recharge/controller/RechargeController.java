package com.example.hotsix_be.payment.recharge.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossWebhookRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.recharge.dto.response.RechargePageResponse;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.openapi.RechargeApi;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recharge")
@Slf4j
public class RechargeController implements RechargeApi {
    private final RechargeService rechargeService;

    @GetMapping("/me")
    @MemberOnly
    public ResponseEntity<ResponseDto<Page<RechargePageResponse>>> showMyRecharge(
            final Pageable pageable,
            @Auth final Accessor accessor
    ) {
        Page<RechargePageResponse> rechargePageResponse = rechargeService.getRechargePageResponse(accessor, pageable);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "캐시 충전 신청 내역 조회 성공", null,
                null, rechargePageResponse
        ));
    }

    @PostMapping("/request")
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> recharge(
            @RequestBody final TossConfirmRequest tossConfirmRequest,
            @Auth final Accessor accessor
    ) {
        rechargeService.doRecharge(tossConfirmRequest, accessor, null);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "충전 신청 성공", null,
                        null, new EmptyResponse()));
    }

    @PostMapping("/tossWebhook")
    public ResponseEntity<ResponseDto<EmptyResponse>> tossWebhook(
            @RequestBody TossWebhookRequest tossWebhookRequest
    ) {
        String orderId = tossWebhookRequest.getOrderId();
        String secret = tossWebhookRequest.getSecret();
        Recharge recharge = rechargeService.findByOrderId(orderId);

        if (!secret.equals(recharge.getSecret())) throw new PaymentException(INVALID_REQUEST);

        rechargeService.processRecharge(recharge, 0L);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "무통장 입금을 성공하셨습니다.", null,
                        null, new EmptyResponse())
        );
    }

    @PatchMapping("/{orderId}/cancel")
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> cancelRecharge(
            @PathVariable String orderId,
            @Auth Accessor accessor
    ) {
        Recharge recharge = rechargeService.findByOrderIdAndMemberId(orderId, accessor.getMemberId());

        rechargeService.cancelRecharge(recharge);

        return ResponseEntity.ok(new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "충전 신청 취소가 완료되었습니다.", null,
                        null, new EmptyResponse()
                )
        );
    }
}
