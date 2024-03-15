package com.example.hotsix_be.reservation.repository;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    Page<Reservation> findByMemberIdAndIsPaidTrueOrderByIdDesc(Pageable pageable, Long memberId);

    List<Reservation> findAllByHotelIdAndIsPaidTrue(Long hotelId);

    Optional<Reservation> findByIdAndIsPaidTrue(Long reserveId);

    Optional<Reservation> findByIdAndIsPaidFalse(Long reserveId);

    Optional<Reservation> findByOrderIdContainingAndMember(String orderId, Member member);

    // 정산용 메소드
    Page<Reservation> findBySettleDateNullAndCheckOutDateLessThanEqualAndCancelDateNull(LocalDate endDay, Pageable pageable);

    Optional<Reservation> findFirstByMemberAndIsPaidTrue(Member member);

    Page<Reservation> findByHostAndCancelDateNull(Member host, Pageable pageable);

    Optional<Reservation> findByOrderIdContainingAndHostId(String orderId, Long hostId);
}
