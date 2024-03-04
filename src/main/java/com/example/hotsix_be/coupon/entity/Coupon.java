package com.example.hotsix_be.coupon.entity;

import com.example.hotsix_be.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private CouponType couponType;

    private Double discountRate; // 할인율

    @ManyToOne
    private Member member;

}
