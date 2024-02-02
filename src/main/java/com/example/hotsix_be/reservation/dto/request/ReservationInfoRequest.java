package com.example.hotsix_be.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class ReservationInfoRequest {
	private Long numOfGuests;
	private LocalDate checkInDate;
	private LocalDate checkOutDate;
	private Long price;
	private boolean isPaid;
}
