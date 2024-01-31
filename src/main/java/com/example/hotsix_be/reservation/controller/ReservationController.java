package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationCreateResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reserve")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/detail/{reserveId}")
    public ResponseEntity<?> getReservationDetail(@PathVariable(value = "reserveId") final Long reserveId) {
        ReservationDetailResponse reservationDetailResponse = reservationService.findById(reserveId);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "예약 상세 조회 성공", null,
                null, reservationDetailResponse));
    }

    @PostMapping("/{hotelId}")
    public ResponseEntity<?> reserveHotel(
            @PathVariable(value = "hotelId") final Long hotelId,
            @RequestBody final ReservationInfoRequest reservationInfoRequest
    ) {
        Reservation reservation = reservationService.save(hotelId, reservationInfoRequest);
        ReservationCreateResponse reservationCreateResponse = ReservationCreateResponse.of(reservation);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 내역이 생성되었습니다.", null,
                        null, reservationCreateResponse
                )
        );
    }
}
