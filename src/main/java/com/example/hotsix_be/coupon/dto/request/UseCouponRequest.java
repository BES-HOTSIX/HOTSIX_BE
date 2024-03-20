package com.example.hotsix_be.coupon.dto.request;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.coupon.dto.response.CouponIssueResponse;
import com.example.hotsix_be.coupon.entity.Coupon;
import com.example.hotsix_be.coupon.entity.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class UseCouponRequest {

    private CouponType couponType;
    private Long discountAmount;


}
