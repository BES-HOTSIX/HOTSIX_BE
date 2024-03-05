package com.example.hotsix_be.coupon.repository;

import com.example.hotsix_be.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
