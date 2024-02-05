package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.dto.response.ReservedDatesOfHotelResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
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
    public Page<ReservationDetailResponse> findByMemberIdAndIsPaid(Long memberId, int page) {
        Pageable pageable = Pageable.ofSize(4).withPage(page);

        return reservationRepository.findByMemberIdAndIsPaidTrueOrderByIdDesc(pageable, memberId)
                .map(reservation -> ReservationDetailResponse.of(
                        reservation.getHotel(),
                        reservation
                ));
    }

	public ReservationDetailResponse findPaidById(final Long reserveId, final Long memberId) {
		Reservation reservation = findPaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		if (!memberId.equals(reservation.getMember().getId()))
			throw new AuthException(INVALID_AUTHORITY);

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}

	public ReservationDetailResponse getDetailById(final Reservation reservation, final Long memberId) {
		// 조회하는 사람이 본인이 아닐 경우 Exception 호출
		if (!memberId.equals(reservation.getMember().getId()))
			throw new AuthException(INVALID_AUTHORITY);

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}

	public ReservationDetailResponse getPaidDetailById(final Long reserveId, final Long memberId) {
		Reservation reservation = findPaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		return getDetailById(reservation, memberId);
	}

	public ReservationDetailResponse getUnpaidDetailById(final Long reserveId, final Long memberId) {
		Reservation reservation = findUnpaidById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		return getDetailById(reservation, memberId);
	}

	public Optional<Reservation> findById(final Long reserveId) {
		return reservationRepository.findById(reserveId);
	}

	// isPaid 가 false 인지 확인 (true 일 경우 Optional 로 감싼 null 을 반환)
	public Optional<Reservation> findUnpaidById(final Long reserveId) {
		return findById(reserveId).filter(reservation -> !reservation.isPaid());
	}

	// isPaid 가 true 인지 확인 (false 일 경우 Optional 로 감싼 null 을 반환)
	public Optional<Reservation> findPaidById(final Long reserveId) {
		return findById(reserveId).filter(reservation -> reservation.isPaid());
	}

	@Transactional
	public Reservation save(final Long hotelId, final ReservationInfoRequest reservationInfoRequest, final Long memberId) {
		Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

		Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(INVALID_AUTHORITY));

		final Reservation reservation = new Reservation(
				reservationInfoRequest.getCheckInDate(),
				reservationInfoRequest.getCheckOutDate(),
				reservationInfoRequest.getNumOfGuests(),
				reservationInfoRequest.getPrice(),
				reservationInfoRequest.isPaid(),
				hotel,
				member
		);

        return reservationRepository.save(reservation);
    }

	public ReservedDatesOfHotelResponse findAllByHotelIdAndIsPaidTrue(final Long hotelId) {
		List<Reservation> reservations = reservationRepository.findAllByHotelIdAndIsPaidTrue(hotelId);

		return ReservedDatesOfHotelResponse.of(
				reservations
		);
	}
}