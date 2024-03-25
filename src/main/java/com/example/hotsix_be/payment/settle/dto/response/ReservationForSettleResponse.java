package com.example.hotsix_be.payment.settle.dto.response;

import com.example.hotsix_be.payment.settle.utils.SettleUt;
import com.example.hotsix_be.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "Settle 정산 예정이거나 정산된 예약 조회")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationForSettleResponse {

    @Schema(description = "주문 고유 식별 코드", example = "o8sJILLP1EP6V1nLksCBL")
    private final String orderId;

    @Schema(description = "정산 예정 금액", example = "900000")
    private final Long expectedAmount;

    @Schema(description = "총 가격", example = "1000000")
    private final Long price;

    @Schema(description = "수수료", example = "100000")
    private final Long commission;

    @Schema(description = "실 정산액", example = "0")
    private final Long settledAmount;

    @Schema(description = "정산 예정일", example = "2024-01-01")
    private final LocalDate settleDue;

    public static ReservationForSettleResponse of(
            final Reservation reservation
    ) {
        Long price = reservation.getPrice();
        Long commission = SettleUt.calculateCommission(price);
        Long expectedAmount = price - commission;
        Long settledAmount = reservation.getSettleDate() != null ? expectedAmount : 0;
        LocalDate settleDue = reservation.getSettleDate() != null ? reservation.getSettleDate() : SettleUt.getExpectedSettleDate(reservation.getCheckOutDate());

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
