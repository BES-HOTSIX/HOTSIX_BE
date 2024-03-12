package com.example.hotsix_be.reservation.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.dto.request.ReservationInfoRequest;
import com.example.hotsix_be.reservation.dto.response.HostReservationPageResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationCreateResponse;
import com.example.hotsix_be.reservation.dto.response.ReservationInfoResponse;
import com.example.hotsix_be.reservation.dto.response.ReservedDatesOfHotelResponse;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        ReservationInfoResponse reservationInfoResponse = reservationService.getInfoById(reserveId,
                accessor.getMemberId());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "예약 상세 조회 성공", null,
                null, reservationInfoResponse)
        );
    }

    @PostMapping("/{hotelId}")
    @MemberOnly
    public ResponseEntity<?> reserveHotel(
            @PathVariable(value = "hotelId") final Long hotelId,
            @RequestBody final ReservationInfoRequest reservationInfoRequest,
            @Auth final Accessor accessor
    ) {
        ReservationCreateResponse reservationCreateResponse = reservationService.save(hotelId, reservationInfoRequest,
                accessor.getMemberId());

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
        ReservedDatesOfHotelResponse reservedDatesOfHotelResponse = reservationService.findAllByHotelIdAndIsPaidTrue(
                hotelId);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "해당 숙소의 예약된 날짜를 모두 불러왔습니다.", null,
                        null, reservedDatesOfHotelResponse
                )
        );
    }

    @PutMapping("/{hotelId}/{reserveId}")
    @MemberOnly
    public ResponseEntity<?> reserveHotelByReserveId(
            @PathVariable(value = "hotelId") final Long hotelId,
            @PathVariable(value = "reserveId") final Long reserveId,
            @RequestBody final ReservationInfoRequest reservationInfoRequest,
            @Auth final Accessor accessor
    ) {
        ReservationCreateResponse reservationCreateResponse = reservationService.modifyByReserveId(hotelId, reserveId,
                reservationInfoRequest, accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "예약 내역이 수정되었습니다.", null,
                        null, reservationCreateResponse
                )
        );
    }

    @GetMapping("/hosts/{hotelId}")
    @MemberOnly
    public ResponseEntity<?> getHostsReservationsByCheckoutDate(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "year") final int year,
            @RequestParam(value = "month") final int month,
            @PathVariable(value = "hotelId") final Long hotelId
    ) {
        Page<HostReservationPageResponse> reservationsByHotelAndCheckoutMonth = reservationService.findReservationsByHotelAndCheckoutMonth(
                hotelId, year, month, page);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "호스트의 숙소 예약 현황 조회가 성공적으로 완료되었습니다.", null,
                        null, reservationsByHotelAndCheckoutMonth
                )
        );
    }
}
