package com.example.hotsix_be.cashlog.controller;

import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.service.CashLogService;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cashLog")
public class CashLogController {
    private final CashLogService cashLogService;
    private final ReservationService reservationService;

    @PostMapping("/addCash")
    public ResponseEntity<?> addCash(@RequestBody AddCashRequest addCashRequest) {
        cashLogService.addCash(addCashRequest, EventType.충전__무통장입금);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "무통장 입금이 완료되었습니다.", null,
                        null, null
                )
        );
    }
}
