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

import java.time.LocalDate;
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
        Reservation reservation = findPaidById(reserveId);

        return getDetailById(reservation, memberId);
    }

    public ReservationDetailResponse getUnpaidDetailById(final Long reserveId, final Long memberId) {
        Reservation reservation = findUnpaidById(reserveId);

        return getDetailById(reservation, memberId);
    }

    // isPaid 가 false 인지 확인 (true 일 경우 Optional 로 감싼 null 을 반환)
    public Reservation findUnpaidById(final Long reserveId) {
        return reservationRepository.findByIdAndIsPaidFalse(reserveId).orElseThrow(
                () -> new ReservationException(NOT_FOUND_RESERVATION_ID));
    }

    // isPaid 가 true 인지 확인 (false 일 경우 Optional 로 감싼 null 을 반환)
    public Reservation findPaidById(final Long reserveId) {
        return reservationRepository.findByIdAndIsPaidTrue(reserveId).orElseThrow(
                () -> new ReservationException(NOT_FOUND_RESERVATION_ID));
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

    private ReservationInfoResponse getInfoByReservation(final Reservation reservation, final Long memberId) {
        // 조회하는 사람이 본인이 아닐 경우 Exception 호출
        // 추가 : 조회하는 사람이 본인이나 해당 호텔의 호스트가 아닐 경우 Exception 호출 (김경환)
        if (!memberId.equals(reservation.getMember().getId()) && !memberId.equals(reservation.getHost().getId())) {
            throw new AuthException(INVALID_AUTHORITY);
        }

        Review review = reviewRepository.findByReservationId(reservation.getId()).orElse(null);

        return ReservationInfoResponse.of(
                reservation.getHotel(),
                reservation,
                review
        );
    }

    public ReservationInfoResponse getInfoById(final Long reserveId, final Long memberId) {
        Reservation reservation = findPaidById(reserveId);

        return getInfoByReservation(reservation, memberId);
    }

    public ReservationInfoResponse getInfoByOrderIdAndHostId(final String orderId, final Long hostId) {
        Reservation reservation = findByOrderIdAndHostId(orderId, hostId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        return getInfoByReservation(reservation, hostId);
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

    public Reservation findByOrderIdAndMember(final String orderId, final Member member) {
        return reservationRepository.findByOrderIdContainingAndMember(orderId, member).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));
    }

    public Optional<Reservation> findByOrderIdAndHostId(final String orderId, final Long hostId) {
        return reservationRepository.findByOrderIdContainingAndHostId(orderId, hostId);
    }

    public Long findExpectedSettleByHost(final Member host) {
        return reservationRepository.sumPriceByMemberIdAndSettleDateNull(host);
    }

    public Page<Reservation> findByHostIdAndParamsAndCancelDateNotNull(
            final Member host,
            final LocalDate startDate,
            final LocalDate endDate,
            final String settleKw,
            final Pageable pageable
    ) {
        return reservationRepository.findByHostIdAndParamsAndCancelDateNotNull(host, startDate, endDate, settleKw, pageable);
    }

    public HostReservationSummaryResponse findReservationsByHotelAndCheckoutMonth(final Long hotelId, final int year, final int month,
                                                                                  final int page) {
        Pageable pageable = Pageable.ofSize(10).withPage(page);

        Page<HostReservationPageResponse> hotelsByCheckoutMonth = reservationRepository.findReservationsByHotelAndCheckoutMonth(hotelId,
                        year, month, pageable)
                .map(reservation -> {
                    return HostReservationPageResponse.of(
                            reservation.getHotel(),
                            reservation
                    );
                });

        // 전체 예약 대상 총 매출과 건수 계산
        Long totalSales = reservationRepository.calculateTotalSales(hotelId, year, month);
        Long completedReservationCount = reservationRepository.countCompletedReservations(hotelId, year, month);

        return HostReservationSummaryResponse.of(hotelsByCheckoutMonth, totalSales, completedReservationCount);
    }
}