package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.cashlog.entity.CashLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class TestResponse {
    private final String eventType;
    private final Long price;
//    private final long memberId;
    private final Long reservationId;

    public static TestResponse of(final CashLog cashLog) {
        return new TestResponse(
                cashLog.getEventType().toString(),
                cashLog.getPrice(),
                null
        );
    }
}
