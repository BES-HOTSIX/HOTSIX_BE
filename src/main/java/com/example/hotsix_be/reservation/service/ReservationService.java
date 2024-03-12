package com.example.hotsix_be.reservation.service;


import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.*;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import com.example.hotsix_be.review.entity.Review;
import com.example.hotsix_be.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public Page<MemberReservationResponseDTO> findByMemberIdAndIsPaid(Long memberId, int page) {
        Pageable pageable = Pageable.ofSize(4).withPage(page);

        return reservationRepository.findByMemberIdAndIsPaidTrueOrderByIdDesc(pageable, memberId)
                .map(reservation -> {
                    return MemberReservationResponseDTO.of(
                            reservation.getHotel(),
                            reservation,
                            reviewRepository.findByReservationId(reservation.getId()).orElse(null)
                    );
                });
    }

    public ReservationDetailResponse getDetailById(final Reservation reservation, final Long memberId) {
        // 조회하는 사람이 본인이 아닐 경우 Exception 호출
        if (!memberId.equals(reservation.getMember().getId())) {
            throw new AuthException(INVALID_AUTHORITY);
        }

        return ReservationDetailResponse.of(
                reservation.getHotel(),
                reservation
        );
    }

    public ReservationDetailResponse getPaidDetailById(final Long reserveId, final Long memberId) {
        Reservation reservation = findPaidById(reserveId).orElseThrow(
                () -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        return getDetailById(reservation, memberId);
    }

    public ReservationDetailResponse getUnpaidDetailById(final Long reserveId, final Long memberId) {
        Reservation reservation = findUnpaidById(reserveId).orElseThrow(
                () -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        return getDetailById(reservation, memberId);
    }

    // isPaid 가 false 인지 확인 (true 일 경우 Optional 로 감싼 null 을 반환)
    public Optional<Reservation> findUnpaidById(final Long reserveId) {
        return reservationRepository.findByIdAndIsPaidFalse(reserveId);
    }

    // isPaid 가 true 인지 확인 (false 일 경우 Optional 로 감싼 null 을 반환)
    public Optional<Reservation> findPaidById(final Long reserveId) {
        return reservationRepository.findByIdAndIsPaidTrue(reserveId);
    }

    @Transactional
    public ReservationCreateResponse save(final Long hotelId, final ReservationInfoRequest reservationInfoRequest,
                                          final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        final Reservation reservation = new Reservation(
                reservationInfoRequest.getCheckInDate(),
                reservationInfoRequest.getCheckOutDate(),
                reservationInfoRequest.getNumOfGuests(),
                reservationInfoRequest.getPrice(),
                reservationInfoRequest.isPaid(),
                hotel,
                member
        );

        Reservation reservationResult = reservationRepository.save(reservation);

        return ReservationCreateResponse.of(reservationResult);
    }

    public ReservedDatesOfHotelResponse findAllByHotelIdAndIsPaidTrue(final Long hotelId) {
        List<Reservation> reservations = reservationRepository.findAllByHotelIdAndIsPaidTrue(hotelId);

        return ReservedDatesOfHotelResponse.of(
                reservations
        );
    }

    public ReservationInfoResponse getInfoById(final Long reserveId, final Long memberId) {
        Reservation reservation = findPaidById(reserveId).orElseThrow(
                () -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        // 조회하는 사람이 본인이 아닐 경우 Exception 호출
        if (!memberId.equals(reservation.getMember().getId())) {
            throw new AuthException(INVALID_AUTHORITY);
        }

        Review review = reviewRepository.findByReservationId(reserveId).orElse(null);

        return ReservationInfoResponse.of(
                reservation.getHotel(),
                reservation,
                review
        );
    }

    @Transactional
    public ReservationCreateResponse modifyByReserveId(final Long hotelId, final Long reserveId,
                                                       final ReservationInfoRequest reservationInfoRequest,
                                                       final Long memberId) {
        memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        Reservation reservation = reservationRepository.findById(reserveId)
                .orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));
        reservation.update(reservationInfoRequest, hotel);

        return ReservationCreateResponse.of(reservation);
    }

    public Optional<Reservation> findByOrderId(String orderId) {
        return reservationRepository.findByOrderId(orderId);
    }

    public Page<HostReservationPageResponse> findReservationsByHotelAndCheckoutMonth(final Long hotelId, final int year, final int month,
                                                                                     final int page) {
        Pageable pageable = Pageable.ofSize(5).withPage(page);

        return reservationRepository.findReservationsByHotelAndCheckoutMonth(hotelId, year, month, pageable)
                .map(reservation -> {
                    return HostReservationPageResponse.of(
                            reservation.getHotel(),
                            reservation
                    );
                });
    }
}