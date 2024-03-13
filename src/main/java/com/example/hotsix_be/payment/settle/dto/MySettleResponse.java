package com.example.hotsix_be.payment.settle.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MySettleResponse {

    private final Long settledCash;

    private final LocalDate settleDate;

    private final Long expectedTotalSettleAmount;


    public static MySettleResponse of(
            final Long settledCash,
            final LocalDate settleDate,
            final Long expectedTotalSettleAmount
    ) {
        return new MySettleResponse(
                settledCash,
                settleDate,
                expectedTotalSettleAmount
        );
    }
}
