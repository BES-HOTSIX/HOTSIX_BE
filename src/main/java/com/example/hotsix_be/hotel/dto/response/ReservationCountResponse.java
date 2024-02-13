package com.example.hotsix_be.hotel.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.reservation.entity.Reservation;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationCountResponse {

    private final Long reservationsCount;

    public static ReservationCountResponse of(final List<Reservation> reservations) {
        return new ReservationCountResponse((long) reservations.size());
    }

}
