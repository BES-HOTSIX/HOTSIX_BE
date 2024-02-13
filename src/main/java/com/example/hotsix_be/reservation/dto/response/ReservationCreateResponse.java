package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationCreateResponse {
    private final Long id;

    public static ReservationCreateResponse of(final Reservation reservation) {
        return new ReservationCreateResponse(
                reservation.getId()
        );
    }
}
