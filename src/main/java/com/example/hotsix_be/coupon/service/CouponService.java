package com.example.hotsix_be.coupon.service;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.coupon.dto.response.CouponIssueResponse;
import com.example.hotsix_be.coupon.entity.Coupon;
import com.example.hotsix_be.coupon.entity.CouponRecord;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.coupon.exception.CouponException;
import com.example.hotsix_be.coupon.repository.CouponRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import java.util.List;
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

        if (alreadHasNewCustomerCoupon(member, CouponType.신규회원)) {
            throw new CouponException(ExceptionCode.ALREADY_ISSUED_FIRST_RESERVATION_COUPON);
        }

        Coupon coupon = new Coupon(CouponType.신규회원, member);
        couponRepository.save(coupon);
    }

    public List<CouponIssueResponse> getCouponsByMemberId(final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        List<Coupon> coupons = couponRepository.findByMember(member);

        return coupons.stream().map(CouponIssueResponse::of).toList();
    }


    private boolean alreadHasNewCustomerCoupon(Member member, CouponType couponType) {
        return couponRepository.findByMemberAndCouponType(member, couponType).isPresent();
    }

    public void deleteCoupon(Long memberId, CouponType couponType) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        // 멤버와 쿠폰 레코드를 기반으로 쿠폰 찾기
        Coupon coupon = couponRepository.findByMemberAndCouponType(member, couponType)
                .orElseThrow(() -> new CouponException(ExceptionCode.NOT_FOUND_COUPON_TYPE));

        // 쿠폰 레코드 생성

    }
}