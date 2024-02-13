package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ConfirmResponse {
    // 본인 외의 접근을 막을 로직을 위한 memberId
    private final Long memberId;
    private final Long reserveId;
    private final String orderId;
    private final String hotelNickname;
    private final Long price;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final String eventType;

    public static ConfirmResponse of(
            final CashLog cashLog,
            final Reservation reservation,
            final Hotel hotel
    ) {
        Long price = cashLog.getPrice();
        if (price < 0) price *= -1;

        return new ConfirmResponse(
                reservation.getMember().getId(),
                reservation.getId(),
                reservation.getOrderId(),
                hotel.getNickname(),
                price,
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                cashLog.getEventType().getStatus()
        );
    }
}
