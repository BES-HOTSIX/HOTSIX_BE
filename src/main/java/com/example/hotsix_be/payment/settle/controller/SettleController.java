package com.example.hotsix_be.payment.settle.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.settle.dto.response.MySettleResponse;
import com.example.hotsix_be.payment.settle.dto.response.ReservationForSettleResponse;
import com.example.hotsix_be.payment.settle.service.SettleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settle")
public class SettleController {
    private final SettleService settleService;

    @GetMapping("/me")
    @MemberOnly
    public ResponseEntity<ResponseDto<MySettleResponse>> showSettlePage(@Auth Accessor accessor) {
        MySettleResponse res = settleService.getMySettleByMemberId(accessor.getMemberId());

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "내 정산 정보 조회 성공", null,
                null, res
        ));
    }

    @GetMapping("/me/list")
    @MemberOnly
    public ResponseEntity<ResponseDto<Page<ReservationForSettleResponse>>> showSettleList(
            @Auth Accessor accessor,
            final Pageable pageable,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate,
            @RequestParam String settleKw
    ) {
        Page<ReservationForSettleResponse> resPage = settleService.getReserveForSettleByMemberId(accessor.getMemberId(), startDate, endDate, settleKw, pageable);

        return ResponseEntity.ok(new ResponseDto<>(
                HttpStatus.OK.value(),
                "내 정산 리스트 조회 성공", null,
                null, resPage
        ));
    }
}
