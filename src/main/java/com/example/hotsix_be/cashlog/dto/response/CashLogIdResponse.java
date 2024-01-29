package com.example.hotsix_be.cashlog.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogIdResponse {
    private final Long cashLogId;

    public static CashLogIdResponse of(final long cashLogId) {
        return new CashLogIdResponse(
                cashLogId
        );
    }
}
