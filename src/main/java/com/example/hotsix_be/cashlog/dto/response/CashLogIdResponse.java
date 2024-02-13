package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.payment.payment.dto.request.TossPaymentRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogIdResponse {
    private final Long cashLogId;
    private final TossPaymentRequest tossPaymentRequest;

    public static CashLogIdResponse of(final Long cashLogId, final TossPaymentRequest tossPaymentRequest) {
        return new CashLogIdResponse(
                cashLogId,
                tossPaymentRequest
        );
    }
    public static CashLogIdResponse of(final Long cashLogId) {
        return CashLogIdResponse.of(cashLogId, null);
    }
}
