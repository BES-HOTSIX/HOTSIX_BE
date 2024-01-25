package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationCancelResponse {
	private final String hotelNickname;
	private final String hotelDescription;
	private final String hotelPhotoUrl;
	//	private final String hotelHost;
	private final LocalDateTime checkInDate;
	private final LocalDateTime checkOutDate;
	private final LocalDateTime cancelDate;
	private final int numOfGuests;
	private final long paidPrice;

	public static ReservationCancelResponse of(final Hotel hotel, final Reservation reservation) {
		String imageUrl = "";
		if (!hotel.getImages().isEmpty()) {
			imageUrl = hotel.getImages().get(0).getUrl(); // 첫 번째 이미지의 URL을 가져옵니다.
		}

		return new ReservationCancelResponse(
				hotel.getNickname(),
				hotel.getDescription(),
				imageUrl,
				// hotel.getOwner().getUsername(), // 필요한 경우 주석 해제
				reservation.getCheckInDate(),
				reservation.getCheckOutDate(),
				reservation.getCancelDate(),
				reservation.getGuests(),
				reservation.getPrice()
		);
	}
}
