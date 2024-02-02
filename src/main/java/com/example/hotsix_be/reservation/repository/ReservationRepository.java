package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByMemberIdAndIsPaidTrueOrderByIdDesc(Pageable pageable, Long memberId);
}
