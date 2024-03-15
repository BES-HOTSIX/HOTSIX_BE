package com.example.hotsix_be.coupon.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.coupon.entity.Coupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CouponIssueResponse {

    private final String couponType;
    private final Double discountRate;

    public static CouponIssueResponse of(final Coupon coupon) {
        return new CouponIssueResponse(
                coupon.getCouponType().name(),
                coupon.getCouponType().getDiscountRate()
        );
    }

}
