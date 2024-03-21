package com.example.hotsix_be.payment.refund.service;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.refund.entity.Refund;
import com.example.hotsix_be.payment.refund.repository.RefundRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;
    private final CashLogService cashLogService;

    @Transactional
    public Refund doRefund(final Reservation reservation, final Long discountAmount) {
        Member member = reservation.getMember();
        Long amount = reservation.getPrice();
        String orderId = reservation.getOrderId();
        Member refunder = reservation.getHotel().getOwner();

//        addCash(reservation.getHotel().getOwner(), reservation.getPrice() * -1, reservation, 정산__예약취소);

        Refund refund = Refund.builder()
                .reservation(reservation)
                .refunder(refunder)
                .build();

        refund = cashLogService.addCashLog(
                member,
                amount,
                orderId,
                EventType.취소__예치금,
                refund,
                discountAmount
        );

        reservation.cancelDone();

        return refundRepository.save(refund);
    }
}
