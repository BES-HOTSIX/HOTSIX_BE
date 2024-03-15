package com.example.hotsix_be.payment.settle.service;

import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.service.CashLogService;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.settle.dto.MySettleResponse;
import com.example.hotsix_be.payment.settle.dto.ReservationForSettleResponse;
import com.example.hotsix_be.payment.settle.entity.Settle;
import com.example.hotsix_be.payment.settle.repository.SettleRepository;
import com.example.hotsix_be.payment.settle.utils.SettleUt;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettleService {
    private final CashLogService cashLogService;
    private final MemberService memberService;
    private final ReservationService reservationService;
    private final SettleRepository settleRepository;

    // Reservation 을 Settle로 처리 (ItemProcessor)
    @Transactional
    public Settle doSettle(final Reservation reservation) {
        // 이미 정산된 데이터일 경우 예외 발생
        if (reservation.isSettled()) throw new PaymentException(ExceptionCode.ALREADY_BEEN_SETTLED);

        Member host = reservation.getHost(); // 호스트
        Long price = reservation.getPrice(); // 원래 가격
        Long commission = SettleUt.calculateCommission(price); // 수수료
        Long deductedPrice = deductCommission(price, commission); // 수수료를 제한 가격

        Settle settle = Settle.builder()
                .commissionRate(SettleUt.getCommissionRate())
                .commission(commission)
                .totalAmount(price)
                .build();

        settle = cashLogService.addCashLog(
                host,
                deductedPrice,
                reservation.getOrderId(),
                EventType.정산__예치금적립,
                settle
        );

        reservation.settleDone();

        return settle;
    }

    // 총액과 수수료를 파라미터로 받아 공제된 금액 반환
    private Long deductCommission(final Long price, final Long commission) {
        Long deductedPrice = price - commission;

        // 어드민 계정 (Id 가 1인 멤버) 에 수수료 전달
        Member admin = memberService.getMemberById(1L);
        admin.addCash(commission);

        return deductedPrice;
    }

    // 처리된 Settle 을 저장 ( ItemWriter )
    @Transactional
    public Settle save(final Settle settle) {
        return settleRepository.save(settle);
    }

    public MySettleResponse getMySettleByMemberId(final Long id) {
        Member host = memberService.getMemberById(id);
        Long restCash = host.getRestCash();
        LocalDate settleDate = SettleUt.getExpectedSettleDate();
        Long expectedTotalSettleAmount = reservationService.findExpectedSettleByHost(host);

        return MySettleResponse.of(restCash, settleDate, expectedTotalSettleAmount);
    }

    public Page<ReservationForSettleResponse> getReserveForSettleByMemberId(
            final Long id,
            final Pageable pageable
            ) {
        Member host = memberService.getMemberById(id);

        // TODO 원한다면 체크아웃 날짜와 체크인 날짜 순으로도 확인할 수 있도록 하기
        Pageable sortedPageable = ((PageRequest) pageable).withSort(Sort.by("createdAt").descending());

        Page<Reservation> reservations = reservationService.findByHostAndCancelDateNull(host, sortedPageable);

        Page<ReservationForSettleResponse> resPage = reservations.map(ReservationForSettleResponse::of);

        return Optional.of(resPage)
                .filter(Slice::hasContent)
                .orElse(Page.empty());
    }
}
