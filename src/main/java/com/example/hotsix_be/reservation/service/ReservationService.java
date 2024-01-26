package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.Optional;
=======
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_RESERVATION_ID;
>>>>>>> 38513d6c132fb2ae4e64dff1e2e9aaa6d7c201bc

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationDetailResponse findById(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}

	// TODO 나중에 위의 메소드와 병합
	public Optional<Reservation> findOpById(long id) {
		return reservationRepository.findById(id);
	}

	public void payDone(Reservation reservation) {
		reservation.toBuilder()
				.isPaid(true);
	}
}
