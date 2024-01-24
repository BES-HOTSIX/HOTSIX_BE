package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reserve")
public class ReservationController {
	private final ReservationService reservationService;

	@GetMapping("/detail/{reserveId}")
	public ResponseEntity<?> getReservationDetail(@PathVariable(value = "reserveId") final Long reserveId) {
		System.out.println(reserveId);
		ReservationDetailResponse reservationDetailResponse = reservationService.findById(reserveId);

		return ResponseEntity.ok(new ResponseDto<>(
				HttpStatus.OK.value(),
				"예약 상세 조회 성공", null,
				null, reservationDetailResponse));
	}
}