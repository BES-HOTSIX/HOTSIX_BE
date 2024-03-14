package com.example.hotsix_be.coupon.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.coupon.entity.Coupon;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CouponResponse {

    private final String couponType;
    private final Double discountRate;

    public static CouponResponse of(final Coupon coupon) {
        return new CouponResponse(
                coupon.getCouponType().name(),
                coupon.getCouponType().getDiscountRate()
        );
    }

}
