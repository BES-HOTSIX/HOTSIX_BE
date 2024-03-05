package com.example.hotsix_be.coupon.entity;

import com.example.hotsix_be.hotel.entity.Hotel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
public class CouponRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double discountAmount;

    @Temporal(TemporalType.DATE)
    private Date discountDate;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel; // 할인이 적용된 숙소

}