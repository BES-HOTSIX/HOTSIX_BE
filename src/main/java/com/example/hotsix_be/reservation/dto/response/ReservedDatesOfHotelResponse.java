package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservedDatesOfHotelResponse {
	private final List<LocalDate> reservedDates;

	public static ReservedDatesOfHotelResponse of(final List<Reservation> reservations) {
		List<LocalDate> reservedDates = new ArrayList<>();

		for (Reservation reservation : reservations) {
			reservedDates.addAll(reservation.getReservedDateRange());
		}

		return new ReservedDatesOfHotelResponse(
				reservedDates
		);
	}
}
