package com.example.hotsix_be.payment.payment.dto.response;

import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class TossEasyPayResponse {
    // TODO 임시 필드
    private final String orderId;

    // TODO 임시 필드
    private final  Long totalAmount;

    public static TossEasyPayResponse of(final TossPaymentRequest req) {
        return new TossEasyPayResponse(
                req.getOrderId(),
                req.getTotalAmount()
        );
    }
}
