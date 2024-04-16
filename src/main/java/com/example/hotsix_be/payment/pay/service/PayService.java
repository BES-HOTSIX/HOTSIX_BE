package com.example.hotsix_be.payment.pay.service;

import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.service.CouponService;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.example.hotsix_be.payment.pay.repository.PayRepository;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.payment.service.TossService;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.example.hotsix_be.common.exception.ExceptionCode.INSUFFICIENT_DEPOSIT;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.결제__예치금;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {
    private final PayRepository payRepository;
    private final CashLogService cashLogService;
    private final RechargeService rechargeService;
    private final TossService tossService;
    private final CouponService couponService;

    // 예치금 사용 결제
    private Pay doPay(final Reservation reservation, final EventType eventType, final Long discountAmount) {

        Member buyer = reservation.getMember();
        Long payPrice = reservation.getPrice();
        Member owner = reservation.getHotel().getOwner();

        Pay pay = Pay.builder()
                .reservation(reservation)
                .recipient(owner)
                .build();

        pay = cashLogService.addCashLog(
                buyer,
                payPrice * -1,
                reservation.getOrderId(),
                eventType,
                pay,
                discountAmount
        );

        // Reservation 객체의 isPaid 값 true 설정
        reservation.payDone();

        return payRepository.save(pay);
    }

    @Transactional
    public CashLog payByCashOnly(final Reservation reservation, final UseCouponRequest useCouponRequest) {
        Long discountAmount = useCouponRequest.getDiscountAmount();

        if (!canPay(reservation, 0L, discountAmount))
            throw new PaymentException(INSUFFICIENT_DEPOSIT);

        reservation.updateOrderId("o" + randomNanoId());

        return doPay(reservation, 결제__예치금, discountAmount);
    }


    // 복합 결제 및 토스페이먼츠 결제
    @Transactional
    public CashLog payByTossPayments(
            final TossConfirmRequest tossConfirmRequest,
            final Reservation reservation,
            final Long discountAmount
    ) {
        if (!canPay(reservation, Long.parseLong(tossConfirmRequest.getAmount()), tossConfirmRequest.getDiscountAmount())) {
            throw new PaymentException(INVALID_REQUEST);
        }

        if (tossConfirmRequest.getDiscountAmount() > 0) {
            couponService.deleteCoupon(
                    reservation,
                    new UseCouponRequest(tossConfirmRequest.getCouponType(), tossConfirmRequest.getDiscountAmount())
            );
        } // 쿠폰 사용 시 쿠폰 삭제

        TossPaymentRequest tossPaymentRequest = tossService.confirmTossPayment(tossConfirmRequest).block();

        Member buyer = reservation.getMember();
        String orderId = tossPaymentRequest.getOrderId();

        // orderId 입력
        reservation.updateOrderId(orderId);

        rechargeService.doRecharge(tossConfirmRequest, buyer, discountAmount);

        return doPay(reservation, EventType.결제__토스페이먼츠, discountAmount);
    }

    public boolean canPay(final Reservation reservation, final Long pgPayPrice, final Long discountAmount) {
        Member member = reservation.getMember();
        Long restCash = member.getRestCash();
        Long price = reservation.getPrice();

        // 중복 결제 예방
        if (reservation.isPaid()) {
            throw new PaymentException(INVALID_REQUEST);
        }

        // 예치금이 충분한지 확인
        return price <= restCash + pgPayPrice + discountAmount;
    }
}
