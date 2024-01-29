package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_RESERVATION_ID;

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

    public List<ReservationDetailResponse> findByMemberId(Long memberId) {
        List<ReservationDetailResponse> reservations = new ArrayList<>();
        reservations = reservationRepository.findByMemberId(memberId).stream().map(
                        reservation -> ReservationDetailResponse.of(
                                reservation.getHotel(),
                                reservation
                        )
                )
                .toList();

        return reservations;
    }
}
