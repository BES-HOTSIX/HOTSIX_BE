package com.example.hotsix_be.payment.cashlog.dto;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class InitCashLogDto<T extends CashLogMarker> {
    private final Member member;
    private final Long price;
    private final String orderId;
    private final EventType eventType;
    private final T cashLogMarker;

    public static <T extends CashLogMarker> InitCashLogDto<T> of(
            final Member member,
            final Long price,
            final String orderId,
            final EventType eventType,
            final T cashLogMarker) {
        return new InitCashLogDto<>(member, price, orderId, eventType, cashLogMarker);
    }
}
