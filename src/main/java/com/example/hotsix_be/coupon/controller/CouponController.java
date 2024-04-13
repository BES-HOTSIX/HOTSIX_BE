package com.example.hotsix_be.coupon.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.dto.response.CouponIssueResponse;
import com.example.hotsix_be.coupon.service.CouponService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 재배포
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
                        "신규 회원 10% 할인 쿠폰이 성공적으로 발행되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/my")
    @MemberOnly
    public ResponseEntity<ResponseDto<List<CouponIssueResponse>>> getCouponsByMemberId(@Auth Accessor accessor) {

        List<CouponIssueResponse> couponsByMemberId = couponService.getCouponsByMemberId(accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "쿠폰 조회에 성공하였습니다.", null,
                        null, couponsByMemberId
                )
        );
    }

}