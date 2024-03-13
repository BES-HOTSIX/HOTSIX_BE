package com.example.hotsix_be.coupon.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/new/issue")
    @MemberOnly
    public ResponseEntity<?> issueFirstReservationCoupon(@Auth Accessor accessor) {

        couponService.issueFirstReservationCoupon(accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "첫 예약 10% 할인 쿠폰이 성공적으로 발행되었습니다.", null,
                        null, null
                )
        );
    }
}
