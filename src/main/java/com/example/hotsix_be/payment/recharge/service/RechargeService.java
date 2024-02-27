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

    @Transactional // 가상계좌 충전 신청
    public void requestVirtualRecharge(final TossPaymentRequest res, final Member member) {
        doRecharge(res, member, 충전__무통장입금);
    }

    @Transactional // 충전 진행
    public void processRecharge(final Recharge recharge) {
        if (recharge.isCancelled()) throw new PaymentException(PAYMENT_NOT_POSSIBLE);

        cashLogService.addCashDone(recharge);
    }

    @Transactional // 간편결제 충전 진행
    public void easyPayRecharge(final TossPaymentRequest res, final Member member) {
        Recharge recharge = doRecharge(res, member, 충전__토스페이먼츠);

        processRecharge(recharge);
    }

    private Recharge doRecharge(final TossPaymentRequest res, final Member member, final EventType eventType) {
        String depositor = null, accountNumber = null, secret = null, bankCode = null;

        if (isVirtual(res)) {
            depositor = res.getVirtualAccount().getCustomerName();
            accountNumber = res.getVirtualAccount().getAccountNumber();
            secret = res.getSecret();
            bankCode = res.getBankCode();
        }

        Recharge recharge = Recharge.builder()
                .depositor(depositor)
                .bankCode(bankCode)
                .accountNumber(accountNumber)
                .secret(secret)
                .build();

        recharge = cashLogService.addCash(
                member,
                res.getTotalAmount(),
                res.getOrderId(),
                eventType,
                recharge
        );

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

        Page<Recharge> pageRecharge = rechargeRepository.findAllByMember(member, sortedPageable);

        return Optional.of(pageRecharge)
                .filter(Slice::hasContent)
                .orElse(Page.empty());
    }

    public Recharge findByOrderIdAndMemberId(final String orderId, final Long memberId) {
        Member member = memberService.getMemberById(memberId);

        return rechargeRepository.findByOrderIdContainingAndMember(orderId, member).orElseThrow(() -> new PaymentException(INVALID_REQUEST));
    }

    @Transactional
    public void cancelRecharge(final Recharge recharge) {
        if (recharge.isPaid()) throw new PaymentException(CANCELLATION_NOT_POSSIBLE);
        recharge.cancelDone();
    }
}
