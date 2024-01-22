package com.example.hotsix_be.cashlog.service;

import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CashLogService {
    private final CashLogRepository cashLogRepository;

    @Transactional
    public CashLog addCash(Member member, long price, Reservation reservation, EventType eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                // reservation 이 null 일 경우 충전 혹은 정산, null 이 아닐 경우 예약 혹은 환불(예약취소)
                .reservation(reservation)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.toBuilder()
                .restCash(newRestCash)
                .build();

        return cashLog;
    }

    @Transactional
    public CashLog addCash(final AddCashRequest addCashRequest, EventType eventType) {
        return addCash(
                null,
                addCashRequest.getPrice(),
                null,
                eventType
        );
    }

    @Transactional
    public CashLog payByCashOnly(Reservation reservation) {
        Member buyer = reservation.getMember();
        long restCash = buyer.getRestCash();
        long payPrice = reservation.getPrice();

        // TODO 5시 결산 시간에 회의 나누고 진행 (예치금이 부족할 경우 날리는 예외)
        if (payPrice > restCash) throw null;

        return addCash(
                buyer,
                reservation.getPrice() * -1,
                reservation,
                EventType.사용__예치금_결제
        );
    }
}
