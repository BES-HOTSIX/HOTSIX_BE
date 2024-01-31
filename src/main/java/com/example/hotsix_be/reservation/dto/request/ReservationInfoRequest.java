package com.example.hotsix_be.reservation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationInfoRequest {
	private Long numOfGuests;
	private LocalDateTime checkInDate;
	private LocalDateTime checkOutDate;
	private Long price;
	private boolean isPaid;
}
