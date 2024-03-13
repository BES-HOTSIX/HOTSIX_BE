package com.example.hotsix_be.payment.settle.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.payment.settle.dto.MySettleResponse;
import com.example.hotsix_be.payment.settle.service.SettleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settle")
public class SettleController {
    private final SettleService settleService;


    @GetMapping("")
    @MemberOnly
    public ResponseEntity<ResponseDto<MySettleResponse>> showSettlePage(@Auth Accessor accessor) {
        settleService.getMySettleByMemberId(accessor.getMemberId());

        return null;
    }
}
