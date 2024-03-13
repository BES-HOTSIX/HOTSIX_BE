package com.example.hotsix_be.coupon.service;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.coupon.entity.Coupon;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.coupon.exception.CouponException;
import com.example.hotsix_be.coupon.repository.CouponRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void issueFirstReservationCoupon(final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        if (!isFirstReservation(member)) {
            throw new CouponException(ExceptionCode.NOT_FIRST_RESERVATION);
        }

        if (alreadyHasFistReservationCoupon(member, CouponType.신규회원)) {
            throw new CouponException(ExceptionCode.ALREADY_ISSUED_FIRST_RESERVATION_COUPON);
        }

        Coupon coupon = new Coupon(CouponType.신규회원, member);
        couponRepository.save(coupon);
    }

    private boolean isFirstReservation(Member member) {
        return reservationRepository.findFirstByMemberAndIsPaidTrue(member).isEmpty();
    }

    private boolean alreadyHasFistReservationCoupon(Member member, CouponType couponType) {
        return couponRepository.findByMemberAndCouponType(member, couponType).isPresent();
    }

}
