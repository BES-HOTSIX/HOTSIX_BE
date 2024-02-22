package com.example.hotsix_be.payment.recharge.service;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
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

    @Transactional
    public Recharge requestVirtualRecharge(final TossPaymentRequest res, final Member member) {
        return doRecharge(res, member, false, 충전__무통장입금);
    }

    @Transactional
    public Recharge processVirtualRecharge(final Recharge recharge) {
        if (recharge.isCancelled()) throw new PaymentException(PAYMENT_NOT_POSSIBLE);

        recharge.payDone();
        cashLogService.addCash(
                recharge.getRecipient(),
                recharge.getAmount(),
                recharge
        );

        return recharge;
    }

    @Transactional
    public Recharge easyPayRecharge(final TossPaymentRequest res, final Member member) {
        Recharge recharge = doRecharge(res, member, true, 충전__토스페이먼츠);

        cashLogService.addCash(member, res.getTotalAmount(), recharge);

        return recharge;
    }

    private Recharge doRecharge(final TossPaymentRequest res, final Member member, final Boolean isPaid, final EventType eventType) {
        String depositor = null;
        String accountNumber = null;
        String secret = null;

        if (isVirtual(res)) {
            depositor = res.getVirtualAccount().getCustomerName();
            accountNumber = res.getVirtualAccount().getAccountNumber();
            secret = res.getSecret();
        }

        Recharge recharge = Recharge.builder()
                .amount(res.getTotalAmount())
                .orderId(res.getOrderId())
                .eventType(eventType)
                .isPaid(isPaid)
                .depositor(depositor)
                .accountNumber(accountNumber)
                .secret(secret)
                .recipient(member)
                .build();

        return rechargeRepository.save(recharge);
    }

    public boolean isVirtual(final TossPaymentRequest res) {
        return res.getMethod().equals("가상계좌");
    }

    public Recharge findByOrderId(final String orderId) {
        return rechargeRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentException(NOT_FOUND_RECHARGE_ID));
    }

    public Page<Recharge> findMyPageList(final Long memberId, final Pageable pageable) {
        Member member = memberService.getMemberById(memberId);

        Pageable sortedPageable = ((PageRequest) pageable).withSort(Sort.by("createdAt").descending());

        Page<Recharge> pageRecharge = rechargeRepository.findAllByRecipient(member, sortedPageable);

        return Optional.of(pageRecharge)
                .filter(Slice::hasContent)
                .orElse(Page.empty());
    }

    public Recharge findByOrderIdAndMemberId(String orderId, Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return rechargeRepository.findByOrderIdContainingAndRecipient(orderId, member).orElseThrow(() -> new PaymentException(INVALID_REQUEST));
    }

    @Transactional
    public void cancelRecharge(Recharge recharge) {
        if (recharge.isPaid()) throw new PaymentException(CANCELLATION_NOT_POSSIBLE);
        recharge.cancelDone();
    }
}
