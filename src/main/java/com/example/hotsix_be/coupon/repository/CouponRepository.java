package com.example.hotsix_be.coupon.repository;

import com.example.hotsix_be.coupon.entity.Coupon;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByMemberAndCouponType(Member member, CouponType couponType);
}
