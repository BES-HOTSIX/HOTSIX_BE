package com.example.hotsix_be.coupon.entity;

import com.example.hotsix_be.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CouponRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long discountAmount;

    private LocalDate usedDate;

    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 할인을 받은 회원

    /*
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel; // 할인이 적용된 숙소
  */

    public CouponRecord(Long discountAmount, LocalDate usedDate, CouponType couponType, Member member) {
        this.discountAmount = discountAmount;
        this.usedDate = usedDate;
        this.couponType = couponType;
        this.member = member;
    }
}