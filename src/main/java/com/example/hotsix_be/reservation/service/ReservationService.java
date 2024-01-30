package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_HOTEL_ID;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_RESERVATION_ID;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final HotelRepository hotelRepository;

	public ReservationDetailResponse findById(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}

	public Reservation save(final Long hotelId, final ReservationInfoRequest reservationInfoRequest) {
		Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

		final Reservation reservation = new Reservation(
				reservationInfoRequest.getCheckInDate(),
				reservationInfoRequest.getCheckOutDate(),
				reservationInfoRequest.getNumOfGuests(),
				reservationInfoRequest.getPrice(),
				hotel,
				reservationInfoRequest.isPaid()
		);

		return reservationRepository.save(reservation);
	}
}
