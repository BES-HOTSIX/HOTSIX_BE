package com.example.hotsix_be.coupon.repository;

import com.example.hotsix_be.coupon.entity.CouponRecord;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRecordRepository extends JpaRepository<CouponRecord, Long> {
    boolean existsByMemberAndCouponType(Member member, CouponType couponType);
}
