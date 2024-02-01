package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ConfirmResponse {
    // 본인 외의 접근을 막을 로직을 위한 memberId
    private final Long memberId;
    private final Long reserveId;
    private final String hotelNickname;
    private final Long price;
    private final LocalDateTime checkInDate;
    private final LocalDateTime checkOutDate;

    // TODO 복합결제의 경우 cashLog 어떻게 생성할지 생각해두기
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
                hotel.getNickname(),
                price,
                reservation.getCheckInDate(),
                reservation.getCheckOutDate()
        );
    }
}
