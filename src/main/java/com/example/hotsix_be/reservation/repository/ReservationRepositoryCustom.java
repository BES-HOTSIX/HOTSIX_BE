package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReservationRepositoryCustom {
    Page<Reservation> findReservationsByHotelAndCheckoutMonth(Long hotelId, int year, int month, Pageable pageable);
    Long calculateTotalSales(Long hotelId, int year, int month);
    Long countCompletedReservations(Long hotelId, int year, int month);

    Long sumPriceByMemberIdAndSettleDateNull(Member host);

    Page<Reservation> findByParamsAndCancelDateNotNull(Member host, LocalDate startDate, LocalDate endDate, String settleKw, Pageable pageable);
}
