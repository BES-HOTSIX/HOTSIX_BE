package com.example.hotsix_be.payment.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class TossWebhookRequest {
    // TODO 임시 필드
    private String orderId;

    // TODO 임시 필드
    private String status;

    // TODO 임시 필드
    private String secret;
}
