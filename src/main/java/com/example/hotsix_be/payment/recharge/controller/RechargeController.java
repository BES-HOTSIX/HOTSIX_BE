package com.example.hotsix_be.payment.recharge.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossWebhookRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.dto.response.RechargeConfirmResponse;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.openapi.RechargeApi;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cashLog/me/recharge")
@Slf4j
public class RechargeController implements RechargeApi {
    private final RechargeService rechargeService;
    private final MemberService memberService;
    private final TossService tossService;

    @GetMapping
    @MemberOnly
    public ResponseEntity<ResponseDto<PageImpl<RechargeConfirmResponse>>> showMyRecharge(
            final Pageable pageable,
            @Auth final Accessor accessor
    ) {
        Long memberId = accessor.getMemberId();

        Page<Recharge> recharges = rechargeService.findMyPageList(memberId, pageable);

        List<RechargeConfirmResponse> rechargeConfirmResponses =
                recharges.stream()
                        .map(RechargeConfirmResponse::of)
                        .toList();

        PageImpl<RechargeConfirmResponse> rechargeConfirmPage =
                new PageImpl<>(
                        rechargeConfirmResponses,
                        pageable,
                        recharges.getTotalElements());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "캐시 충전 신청 내역 조회 성공", null,
                null, rechargeConfirmPage
        ));
    }

    @PostMapping("/request")
    @MemberOnly
    public ResponseEntity<ResponseDto<EmptyResponse>> recharge(
            @RequestBody final TossConfirmRequest tossConfirmRequest,
            @Auth final Accessor accessor
    ) {
        Member member = memberService.getMemberById(accessor.getMemberId());
        TossPaymentRequest tossPaymentRequest = tossService.confirmTossPayment(tossConfirmRequest).block();

        String method = tossPaymentRequest.getMethod();

        if (method.equals("가상계좌")) {
            rechargeService.requestVirtualRecharge(tossPaymentRequest, member);
            return ResponseEntity.ok(
                    new ResponseDto<>(
                            HttpStatus.OK.value(), 
                            "가상계좌 충전 신청 성공", null,
                            null, new EmptyResponse()));
        }
        if (method.equals("간편결제")) {
            rechargeService.easyPayRecharge(tossPaymentRequest, member);
            return ResponseEntity.ok(
                    new ResponseDto<>(
                            HttpStatus.OK.value(),
                            "간편결제 충전 성공", null,
                            null, new EmptyResponse()));
        }
        return ResponseEntity.badRequest().body(
                new ResponseDto<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "캐시 충전 실패", null,
                        null, null));
    }

    @PostMapping("/tossWebhook")
    public ResponseEntity<ResponseDto<EmptyResponse>> tossWebhook(
            @RequestBody TossWebhookRequest tossWebhookRequest
    ) {
        String orderId = tossWebhookRequest.getOrderId();
        String secret = tossWebhookRequest.getSecret();
        Recharge recharge = rechargeService.findByOrderId(orderId);
        if (!secret.equals(recharge.getSecret())) throw new PaymentException(INVALID_REQUEST);
        rechargeService.processVirtualRecharge(recharge);

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
