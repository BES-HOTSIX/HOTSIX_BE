package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final HotelRepository hotelRepository;
	private final MemberRepository memberRepository;

    public Page<ReservationDetailResponse> findByMemberId(Long memberId, int page) {
        Pageable pageable = Pageable.ofSize(4).withPage(page);

        return reservationRepository.findByMemberIdOrderByIdDesc(pageable, memberId)
                .map(reservation -> ReservationDetailResponse.of(
                        reservation.getHotel(),
                        reservation
                ));
    }

	public ReservationDetailResponse findById(Long reserveId, Long memberId) {
		Reservation reservation = reservationRepository.findById(reserveId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

		if (!memberId.equals(reservation.getMember().getId()))
			throw new AuthException(INVALID_AUTHORITY);

		return ReservationDetailResponse.of(
				reservation.getHotel(),
				reservation
		);
	}

    // TODO 나중에 위의 메소드와 병합
    public Optional<Reservation> findOpById(long id) {
        return reservationRepository.findById(id);
    }

    public void payDone(Reservation reservation) {
        reservation.toBuilder()
                .isPaid(true);
    }

	public Reservation save(final Long hotelId, final ReservationInfoRequest reservationInfoRequest, Long memberId) {
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
}