package com.example.hotsix_be.payment.settle.dto.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MySettleResponse {

    private final Long restCash;

    private final LocalDate settleDate;

    private final Long expectedTotalSettleAmount;


    public static MySettleResponse of(
            final Long restCash,
            final LocalDate settleDate,
            final Long expectedTotalSettleAmount
    ) {
        return new MySettleResponse(
                restCash,
                settleDate,
                expectedTotalSettleAmount
        );
    }
}
