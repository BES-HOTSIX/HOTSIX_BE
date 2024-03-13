package com.example.hotsix_be.payment.settle.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationForSettleResponse {

    private final String orderId;

    private final Long expectedAmount;

    private final Long price;

    private final Long commission;

    private final Long settledAmount;

    private final LocalDate settleDue;

    public static ReservationForSettleResponse of(
            final String orderId,
            final Long expectedAmount,
            final Long price,
            final Long commission,
            final Long settledAmount,
            final LocalDate settleDue
    ) {
        return new ReservationForSettleResponse(
                orderId,
                expectedAmount,
                price,
                commission,
                settledAmount,
                settleDue
        );
    }
}
