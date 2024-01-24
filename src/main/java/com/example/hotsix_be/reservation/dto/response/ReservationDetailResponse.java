package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationDetailResponse {
	private final String hotelNickname;
	private final String hotelDescription;
	private final String hotelPhotoUrl;
	//	private final String hotelHost;
	private final LocalDateTime checkInDate;
	private final LocalDateTime checkOutDate;
	private final LocalDateTime createdAt;
	private final int numOfGuests;
	private final long paidPrice;

	public static ReservationDetailResponse of(final Hotel hotel, final Reservation reservation) {

		return new ReservationDetailResponse(
				hotel.getNickname(),
				hotel.getDescription(),
				hotel.getImages().getFirst().getUrl(),
//				hotel.getOwner().getUsername(),
				reservation.getCheckInDate(),
				reservation.getCheckOutDate(),
				reservation.getCreatedAt(),
				reservation.getGuests(),
				reservation.getPrice()
		);
	}
}
