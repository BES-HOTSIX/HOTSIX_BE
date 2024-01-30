package com.example.hotsix_be.cashlog.entity;

import lombok.Getter;

@Getter
public enum EventType {
    충전__무통장입금,
    충전__토스페이먼츠,
    출금__통장입금,
    사용__토스페이먼츠_결제,
    사용__예치금_결제,
    환불__예치금_결제,
    정산__예치금
}
