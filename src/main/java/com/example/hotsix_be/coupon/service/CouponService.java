package com.example.hotsix_be.coupon.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.coupon.dto.request.UseCouponRequest;
import com.example.hotsix_be.coupon.dto.response.CouponIssueResponse;
import com.example.hotsix_be.coupon.entity.Coupon;
import com.example.hotsix_be.coupon.entity.CouponRecord;
import com.example.hotsix_be.coupon.entity.CouponType;
import com.example.hotsix_be.coupon.exception.CouponException;
import com.example.hotsix_be.coupon.repository.CouponRecordRepository;
import com.example.hotsix_be.coupon.repository.CouponRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_MEMBER_BY_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final CouponRepository couponRepository;
    private final CouponRecordRepository couponRecordRepository;

    @Transactional
    public void issueFirstReservationCoupon(final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        if (alreadyHasNewCustomerCoupon(member, CouponType.신규회원)) {
            throw new CouponException(ExceptionCode.ALREADY_ISSUED_FIRST_RESERVATION_COUPON);
        }

        // CouponRecord를 통해 쿠폰 사용 내역도 확인
        if (couponRecordRepository.existsByMemberAndCouponType(member, CouponType.신규회원)) {
            throw new CouponException(ExceptionCode.ALREADY_USED_FIRST_RESERVATION_COUPON);
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


    private boolean alreadyHasNewCustomerCoupon(Member member, CouponType couponType) {
        return couponRepository.findByMemberAndCouponType(member, couponType).isPresent();
    }

    @Transactional
    public void deleteCoupon(Reservation reservation, UseCouponRequest useCouponRequest) {
        Member member = reservation.getMember();

        // 멤버와 쿠폰 레코드를 기반으로 쿠폰 찾기
        Coupon coupon = couponRepository.findByMemberAndCouponType(member, useCouponRequest.getCouponType())
                .orElseThrow(() -> new CouponException(ExceptionCode.NOT_FOUND_COUPON_TYPE));

        // 예약에 쿠폰 할인 금액 반영
        reservation.applyCouponDiscount(useCouponRequest.getDiscountAmount());
        reservationRepository.save(reservation);

        // CouponRecord에 사용 기록 저장 후 쿠폰 삭제
        CouponRecord couponRecord = new CouponRecord(useCouponRequest.getDiscountAmount(), LocalDate.now(),
                useCouponRequest.getCouponType(), member);

        couponRecordRepository.save(couponRecord);

        couponRepository.delete(coupon);
    }


    // 매일 자정에 30일이 초과된 쿠폰 삭제
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteExpiredCoupons() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<Coupon> expiredCoupons = couponRepository.findByIssueDateBefore(thirtyDaysAgo);

        for (Coupon expiredCoupon : expiredCoupons) {
            CouponRecord couponRecord = new CouponRecord(0L, LocalDate.now(),
                    expiredCoupon.getCouponType(), expiredCoupon.getMember());

            couponRecordRepository.save(couponRecord); // 만료된 쿠폰 정보를 CouponRecord에 저장 ( 재발급을 방지하기 위함 )

            couponRepository.delete(expiredCoupon); // 그 다음에 쿠폰 삭제
        }
    }

}