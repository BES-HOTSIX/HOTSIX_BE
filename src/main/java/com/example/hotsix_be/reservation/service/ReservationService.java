package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Optional<Reservation> findById(long id) {
        return reservationRepository.findById(id);
    }

    public void payDone(Reservation reservation) {
        reservation.toBuilder()
                .isPaid(true);
    }
}
