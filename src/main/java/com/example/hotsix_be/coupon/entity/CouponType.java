package com.example.hotsix_be.coupon.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponType {
    신규회원(0.10);

    private final double discountRate;
}
