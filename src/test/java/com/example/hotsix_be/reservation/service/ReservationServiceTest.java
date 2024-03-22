package com.example.hotsix_be.reservation.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.entity.Role;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationCreateResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationInfoResponse;
import com.example.hotsix_be.reservation.dto.response.ReservedDatesOfHotelResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import com.example.hotsix_be.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Transactional
class ReservationServiceTest {
	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private HotelRepository hotelRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private ReviewRepository reviewRepository;

	private Member guest;
	private Member host;
	private Hotel hotel;
	private Reservation reservation;

	@BeforeEach
	void initData() {
		guest = new Member(
				1L,
				"jkeum",
				"jkeum",
				"https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2Fc69962b0-3951-485b-b10a-5bb29576bba8%2F13bbf61e-6916-49f1-9463-89189893b049%2F304758713-ea331acd-c248-45a3-844d-289e2ce3b0d3.jpg?table=block&id=e0b9324e-40dd-4cb1-9d41-172ebba25823&spaceId=c69962b0-3951-485b-b10a-5bb29576bba8&width=2000&userId=696dc99a-391c-4325-9088-fa16896d1aa3&cache=v2"
		);
		guest.assignRole(Role.GUEST);
		host = new Member(
				2L,
				"techit",
				"techit",
				"https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2Fc69962b0-3951-485b-b10a-5bb29576bba8%2F13bbf61e-6916-49f1-9463-89189893b049%2F304758713-ea331acd-c248-45a3-844d-289e2ce3b0d3.jpg?table=block&id=e0b9324e-40dd-4cb1-9d41-172ebba25823&spaceId=c69962b0-3951-485b-b10a-5bb29576bba8&width=2000&userId=696dc99a-391c-4325-9088-fa16896d1aa3&cache=v2"
		);
		host.assignRole(Role.HOST);
		hotel = new Hotel(
				"호텔",
				"서울시 강남구",
				"강남역 1번출구",
				10L,
				10L,
				10L,
				10L,
				List.of("헬스장"),
				"강남호텔",
				"강남호텔입니다.",
				10000L,
				host
		);
		reservation = new Reservation(
				LocalDate.of(2024, 3, 28),
				LocalDate.of(2024, 3, 30),
				2L,
				100000L,
				false,
				hotel,
				guest
		);
	}

	@Test
	@DisplayName("숙소 예약 id로 예약 정보 가져오기 - 성공 케이스")
	void getInfoById_SUCCESS() {
		// given
		given(reservationRepository.findByIdAndIsPaidTrue(reservation.getId())).willReturn(Optional.of(reservation));

		// when
		ReservationInfoResponse result = reservationService.getInfoById(reservation.getId(), guest.getId());

		// then
		assertNotNull(result);
		assertEquals(reservation.getHotel().getId(), result.getHotelId());
		assertEquals(reservation.getHotel().getNickname(), result.getHotelNickname());
		assertEquals(reservation.getCheckInDate(), result.getCheckInDate());
		assertEquals(reservation.getCheckOutDate(), result.getCheckOutDate());
		assertEquals(reservation.getHost().getNickname(), result.getHotelHost());
	}

	@Test
	@DisplayName("숙소 예약 id로 예약 정보 가져오기 - 실패 케이스(예약 정보 없음)")
	void getInfoById_FAIL_RESERVATION_NOT_FOUND() {
		// given
		Long reserveId = 2L; // 존재하지 않는 예약 ID

		given(reservationRepository.findByIdAndIsPaidTrue(reserveId)).willReturn(Optional.empty());

		// when & then
		assertThrows(ReservationException.class, () -> {
			reservationService.getInfoById(reserveId, guest.getId());
		});
	}

	@Test
	@DisplayName("숙소 예약 id로 예약 정보 가져오기 - 실패 케이스(권한 없음)")
	void getInfoById_FAIL_UNAUTHORIZED() {
		// given
		Long memberId = 3L;    // 존재하지 않는 회원 ID

		given(reservationRepository.findByIdAndIsPaidTrue(reservation.getId())).willReturn(Optional.of(reservation));

		// when & then
		assertThrows(AuthException.class, () -> {
			reservationService.getInfoById(reservation.getId(), memberId);
		});
	}

	@Test
	@DisplayName("예약 데이터 생성 - 성공 케이스")
	void save_SUCCESS() {
		// given
		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		Reservation newReservation = new Reservation(
				request.getCheckInDate(),
				request.getCheckOutDate(),
				request.getNumOfGuests(),
				request.getPrice(),
				request.isPaid(),
				hotel,
				guest
		);

		given(memberRepository.findById(guest.getId())).willReturn(Optional.of(guest));
		given(hotelRepository.findById(hotel.getId())).willReturn(Optional.of(hotel));
		given(reservationRepository.save(any(Reservation.class))).willReturn(newReservation);

		// when
		ReservationCreateResponse result = reservationService.save(hotel.getId(), request, guest.getId());

		// then
		assertNotNull(result);
		assertEquals(newReservation.getId(), result.getId());
	}

	@Test
	@DisplayName("예약 데이터 생성 - 실패 케이스(권한 없음)")
	void save_FAIL_UNAUTHORIZED() {
		// given
		Long memberId = 3L;    // 존재하지 않는 회원 ID

		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		assertThrows(AuthException.class, () -> {
			reservationService.save(hotel.getId(), request, memberId);
		});
	}

	@Test
	@DisplayName("예약 데이터 생성 - 실패 케이스(숙소 정보 없음)")
	void save_FAIL_HOTEL_NOT_FOUND() {
		// given
		Long hotelId = 2L;    // 존재하지 않는 숙소 ID

		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		given(memberRepository.findById(guest.getId())).willReturn(Optional.of(guest));
		given(hotelRepository.findById(hotelId)).willReturn(Optional.empty());

		// when & then
		assertThrows(HotelException.class, () -> {
			reservationService.save(hotelId, request, guest.getId());
		});
	}

	@Test
	@DisplayName("예약 데이터 수정 - 성공 케이스")
	void modifyByReserveId_SUCCESS() {
		// given
		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		given(memberRepository.findById(guest.getId())).willReturn(Optional.of(guest));
		given(hotelRepository.findById(hotel.getId())).willReturn(Optional.of(hotel));
		given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));

		// when
		ReservationCreateResponse result = reservationService.modifyByReserveId(hotel.getId(), reservation.getId(), request, guest.getId());

		// then
		assertNotNull(result);
		assertEquals(reservation.getId(), result.getId());
	}

	@Test
	@DisplayName("예약 데이터 수정 - 실패 케이스(권한 없음)")
	void modifyByReserveId_FAIL_UNAUTHORIZED() {
		// given
		Long memberId = 3L;

		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		given(memberRepository.findById(memberId)).willReturn(Optional.empty());

		// when & then
		assertThrows(AuthException.class, () -> {
			reservationService.modifyByReserveId(hotel.getId(), reservation.getId(), request, memberId);
		});
	}

	@Test
	@DisplayName("예약 데이터 수정 - 실패 케이스(숙소 정보 없음)")
	void modifyByReserveId_FAIL_HOTEL_NOT_FOUND() {
		// given
		Long hotelId = 2L;

		ReservationInfoRequest request = new ReservationInfoRequest(
				1L,
				LocalDate.of(2024, 4, 1),
				LocalDate.of(2024, 4, 5),
				200000L,
				false
		);

		given(memberRepository.findById(guest.getId())).willReturn(Optional.of(guest));
		given(hotelRepository.findById(hotelId)).willReturn(Optional.empty());

		// when & then
		assertThrows(HotelException.class, () -> {
			reservationService.modifyByReserveId(hotelId, reservation.getId(), request, guest.getId());
		});
	}

	@Test
	@DisplayName("예약 불가능한 날짜 목록 조회 - 성공 케이스")
	void findAllByHotelIdAndIsPaidTrue_SUCCESS() {
		// given
		ReservedDatesOfHotelResponse response = ReservedDatesOfHotelResponse.of(
				List.of(reservation)
		);

		given(reservationRepository.findAllByHotelIdAndIsPaidTrue(hotel.getId())).willReturn(List.of(reservation));

		// when
		ReservedDatesOfHotelResponse result = reservationService.findAllByHotelIdAndIsPaidTrue(hotel.getId());

		// then
		assertEquals(response.getReservedDates(), result.getReservedDates());
	}
}
