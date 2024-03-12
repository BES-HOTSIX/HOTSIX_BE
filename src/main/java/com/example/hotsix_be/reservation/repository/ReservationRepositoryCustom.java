package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {
    Page<Reservation> findReservationsByHotelAndCheckoutMonth(Long hotelId, int year, int month, Pageable pageable);

}
