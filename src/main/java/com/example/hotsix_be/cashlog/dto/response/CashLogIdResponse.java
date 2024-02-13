package com.example.hotsix_be.cashlog.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogIdResponse {
    private final Long cashLogId;
    private final TossPaymentResponse tossPaymentResponse;

    public static CashLogIdResponse of(final Long cashLogId, final TossPaymentResponse tossPaymentResponse) {
        return new CashLogIdResponse(
                cashLogId,
                tossPaymentResponse
        );
    }
    public static CashLogIdResponse of(final Long cashLogId) {
        return CashLogIdResponse.of(cashLogId, null);
    }
}
