package com.example.hotsix_be.payment.pay.service;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.pay.entity.Pay;
import com.example.hotsix_be.payment.pay.repository.PayRepository;
import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.recharge.service.RechargeService;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.결제__예치금;

@Service
@RequiredArgsConstructor
public class PayService {
    private final PayRepository payRepository;
    private final CashLogService cashLogService;
    private final RechargeService rechargeService;

    // 예치금 사용 결제
    private Pay doPay(final Reservation reservation, final EventType eventType) {
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
                pay
        );

        // Reservation 객체의 isPaid 값 true 설정
        reservation.payDone();

        return payRepository.save(pay);
    }

    @Transactional
    public CashLog payByCashOnly(final Reservation reservation) {
        reservation.updateOrderId("o" + randomNanoId());

        Pay pay = doPay(reservation, 결제__예치금);

        reservation.payDone();

        return pay;
    }



    // 복합 결제 및 토스페이먼츠 결제
    @Transactional
    public CashLog payByTossPayments(
            final TossPaymentRequest tossPaymentRequest,
            final Reservation reservation
    ) {
        Member buyer = reservation.getMember();
        String orderId = tossPaymentRequest.getOrderId();

        // orderId 입력
        reservation.updateOrderId("o" + orderId);

        rechargeService.easyPayRecharge(tossPaymentRequest, buyer);

        return doPay(reservation, EventType.결제__토스페이먼츠); // TODO 토스페이먼츠 결제와 포인트 복합 결제 명확히 하기
    }

    public boolean canPay(final Reservation reservation, final Long pgPayPrice) {
        Member member = reservation.getMember();
        Long restCash = member.getRestCash();
        Long price = reservation.getPrice();

        // 중복 결제 예방
        if (reservation.isPaid()) throw new PaymentException(INVALID_REQUEST);

        // 예치금이 충분한지 확인
        return price <= restCash + pgPayPrice;
    }
}
