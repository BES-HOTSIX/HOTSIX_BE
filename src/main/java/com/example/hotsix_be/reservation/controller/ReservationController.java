package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.ReservationCreateResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationDetailResponse;
import com.example.hotsix_be.reservation.dto.response.ReservedDatesOfHotelResponse;
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
    @MemberOnly
    public ResponseEntity<?> getReservationDetail(
            @PathVariable(value = "reserveId") final Long reserveId,
            @Auth final Accessor accessor
    ) {
        ReservationDetailResponse reservationDetailResponse = reservationService.getPaidDetailById(reserveId, accessor.getMemberId());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "예약 상세 조회 성공", null,
                null, reservationDetailResponse));
    }

    @PostMapping("/{hotelId}")
    @MemberOnly
    public ResponseEntity<?> reserveHotel(
            @PathVariable(value = "hotelId") final Long hotelId,
            @RequestBody final ReservationInfoRequest reservationInfoRequest,
            @Auth final Accessor accessor
    ) {
        Reservation reservation = reservationService.save(hotelId, reservationInfoRequest, accessor.getMemberId());
        ReservationCreateResponse reservationCreateResponse = ReservationCreateResponse.of(reservation);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 내역이 생성되었습니다.", null,
                        null, reservationCreateResponse
                )
        );
    }

    @GetMapping("/reservedDates/{hotelId}")
    public ResponseEntity<?> getReservedDatesOfHotel(
            @PathVariable(value = "hotelId") final Long hotelId
    ) {
        ReservedDatesOfHotelResponse reservedDatesOfHotelResponse = reservationService.findAllByHotelIdAndIsPaidTrue(hotelId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "해당 숙소의 예약된 날짜를 모두 불러왔습니다.", null,
                        null, reservedDatesOfHotelResponse
                )
        );
    }
}
