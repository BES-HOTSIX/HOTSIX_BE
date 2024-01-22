package com.example.hotsix_be.reservation.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationDetailResponse findById(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("예약 상세 조회 실패"));

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}
}
