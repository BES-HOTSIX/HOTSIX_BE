package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
