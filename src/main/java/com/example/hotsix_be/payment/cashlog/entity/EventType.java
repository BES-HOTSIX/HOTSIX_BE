package com.example.hotsix_be.payment.cashlog.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    충전__무통장입금("충전"),
    충전__토스페이먼츠("충전"),
    출금__통장입금("출금"),
    결제__토스페이먼츠("결제"),
    결제__예치금("결제"),
    취소__예치금("취소"),
    정산__예약취소("취소"),
    정산__예치금("결제");

    private final String status;
}
