package com.example.hotsix_be.cashlog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class TossConfirmRequest {

    private String paymentType;

    private String orderId;

    private String amount;

    private String paymentKey;
}
