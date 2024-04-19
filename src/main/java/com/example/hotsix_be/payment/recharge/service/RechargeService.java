package com.example.hotsix_be.payment.recharge.service;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.dto.response.RechargePageResponse;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.repository.RechargeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.충전__무통장입금;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.충전__토스페이먼츠;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RechargeService {
    private final RechargeRepository rechargeRepository;
    private final CashLogService cashLogService;
    private final MemberService memberService;
    private final TossService tossService;

    @Transactional
    public void doRecharge(final TossConfirmRequest tossConfirmRequest, final Member member, final Long discountAmount) {
        TossPaymentRequest tossPaymentRequest = tossService.confirmTossPayment(tossConfirmRequest).block();

        if (isVirtual(tossPaymentRequest)) {
            Recharge recharge = requestVirtualRecharge(tossPaymentRequest, member);
            rechargeRepository.save(recharge);
            return;
        }

        if (isEasyPay(tossPaymentRequest)) {
            Recharge recharge = easyPayRecharge(tossPaymentRequest, member, discountAmount);
            rechargeRepository.save(recharge);
            return;
        }

        throw new PaymentException(INVALID_REQUEST);
    }

    @Transactional
    public void doRecharge(final TossConfirmRequest tossConfirmRequest, final Accessor accessor, final Long discountAmount) {
        Member member = memberService.getMemberById(accessor.getMemberId());

        doRecharge(tossConfirmRequest, member, discountAmount);
    }

    // 가상계좌 충전 신청
    private Recharge requestVirtualRecharge(final TossPaymentRequest req, final Member member) {
        Recharge recharge = Recharge.builder()
                .depositor(req.getVirtualAccount().getCustomerName())
                .bankCode(req.getBankCode())
                .accountNumber(req.getVirtualAccount().getAccountNumber())
                .secret(req.getSecret())
                .build();

        cashLogService.initCashLog(
                member,
                req.getTotalAmount(),
                req.getOrderId(),
                충전__무통장입금,
                recharge
        );

        return recharge;
    }

    // 간편결제 충전 진행
    private Recharge easyPayRecharge(final TossPaymentRequest req, final Member member, final Long discountAmount) {
        Recharge recharge = Recharge.builder()
                .depositor(null)
                .bankCode(null)
                .accountNumber(null)
                .secret(null)
                .build();

        cashLogService.initCashLog(
                member,
                req.getTotalAmount(),
                req.getOrderId(),
                충전__토스페이먼츠,
                recharge
        );

        processRecharge(recharge, discountAmount);

        return recharge;
    }

    @Transactional // 충전 진행
    public void processRecharge(final Recharge recharge, final Long discountAmount) {
        if (recharge.isCancelled()) throw new PaymentException(PAYMENT_NOT_POSSIBLE);

        cashLogService.addCashLogDone(recharge, discountAmount);
    }

    @Transactional
    public void cancelRecharge(final Recharge recharge) {
        if (recharge.isPaid()) throw new PaymentException(CANCELLATION_NOT_POSSIBLE);
        recharge.cancelDone();
    }

    private boolean isVirtual(final TossPaymentRequest req) {
        return req.getMethod().equals("가상계좌");
    }

    private boolean isEasyPay(final TossPaymentRequest req) {
        return req.getMethod().equals("간편결제");
    }

    public Recharge findByOrderId(final String orderId) {
        return rechargeRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentException(NOT_FOUND_RECHARGE_ID));
    }

    private Page<Recharge> findMyPageList(final Long memberId, final Pageable pageable) {
        Member member = memberService.getMemberById(memberId);

        Pageable sortedPageable = ((PageRequest) pageable).withSort(Sort.by("createdAt").descending());

        Page<Recharge> pageRecharge = rechargeRepository.findAllByMember(member, sortedPageable);

        return Optional.of(pageRecharge)
                .filter(Slice::hasContent)
                .orElse(Page.empty());
    }

    public Recharge findByOrderIdAndMemberId(final String orderId, final Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return rechargeRepository.findByOrderIdContainingAndMember(orderId, member).orElseThrow(() -> new PaymentException(NOT_FOUND_RECHARGE_ID));
    }

    public Page<RechargePageResponse> getRechargePageResponse(final Accessor accessor, final Pageable pageable) {
        return findMyPageList(accessor.getMemberId(), pageable).map(RechargePageResponse::of);
    }
}
