package com.example.hotsix_be.reservation.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HostReservationPageResponse {

    private final Long id;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final Long paidPrice;
    private final boolean isPaid;
    private final Long hotelId;
    private final String buyerName;


    public static HostReservationPageResponse of(final Hotel hotel, final Reservation reservation) {

        return new HostReservationPageResponse(
                reservation.getId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getPrice(),
                reservation.isPaid(),
                hotel.getId(),
                reservation.getMember().getNickname()
        );
    }
}
