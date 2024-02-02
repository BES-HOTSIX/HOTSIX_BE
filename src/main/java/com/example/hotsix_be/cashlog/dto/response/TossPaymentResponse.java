package com.example.hotsix_be.cashlog.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class TossPaymentResponse {
    private String orderId;
    private String orderName;
    private Long totalAmount;
}