package com.example.hotsix_be.payment.settle.dto.response;

import com.example.hotsix_be.payment.settle.utils.SettleUt;
import com.example.hotsix_be.reservation.entity.Reservation;
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
            final Reservation reservation
    ) {
        Long price = reservation.getPrice();
        Long commission = SettleUt.calculateCommission(price);
        Long expectedAmount = price - commission;
        Long settledAmount = reservation.getSettleDate() != null ? expectedAmount : 0;
        LocalDate settleDue = reservation.getSettleDate() != null ? reservation.getSettleDate() : SettleUt.getExpectedSettleDate();

        return ReservationForSettleResponse.of(
                reservation.getOrderId(),
                price - commission,
                price,
                commission,
                settledAmount,
                settleDue
        );
    }

    private static ReservationForSettleResponse of(
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
