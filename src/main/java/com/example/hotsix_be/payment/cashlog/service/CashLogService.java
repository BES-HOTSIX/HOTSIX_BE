package com.example.hotsix_be.payment.cashlog.service;


import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CashLogService {
    private final CashLogRepository cashLogRepository;
    private final ReservationService reservationService;
    private final MemberService memberService;

    // 결제 초기 생성 + 마무리
    @Transactional
    public <T extends CashLogMarker> void addCashLog(
            final Member member,
            final Long price,
            final String orderId,
            final EventType eventType,
            final T cashLogMarker,
            final Long discountAmount
    ) {
        T cashLogMarker_ = initCashLog(member, price, orderId, eventType, cashLogMarker);

        addCashLogDone(cashLogMarker_, discountAmount);
    }

    // 결제 초기 생성
    @Transactional
    public <T extends CashLogMarker> T initCashLog(
            final Member member,
            final Long price,
            final String orderId,
            final EventType eventType,
            final T cashLogMarker
    ) {
        CashLog cashLog = CashLog.builder()
                .eventType(eventType)
                .amount(price)
                .orderId(orderId)
                .member(member)
                .build();

        if (cashLogMarker.isInitialized()) throw new PaymentException(ALREADY_BEEN_INITIALIZED);
        cashLogMarker.updateCashLog(cashLog);

        return cashLogMarker;
    }

    // 결제 마무리 ( 보유 캐시 수정 )
    @Transactional
    public <T extends CashLogMarker> T addCashLogDone(final T cashLogMarker, final Long discountAmount) {
        Member member = cashLogMarker.getMember();

        // 금액 이동
        member.addCash(cashLogMarker.getAmount(), discountAmount);

        cashLogMarker.payDone();

        return cashLogMarker;
    }

    public CashLog findById(final Long id) {
        return cashLogRepository.findById(id).orElseThrow(() -> new PaymentException(NOT_FOUND_CASHLOG_ID));
    }

    // 개인 캐시 사용 내역 페이지의 cashLog 리스트
    public MyCashLogResponse findMyPageList(final Accessor accessor, final Pageable pageable) {
        Member member = memberService.getMemberById(accessor.getMemberId());

        Pageable sortedPageable = ((PageRequest) pageable).withSort(Sort.by("createdAt").descending());

        Page<CashLogConfirmResponse> cashLogResPage = cashLogRepository.getCashLogConfirmResForPayByMember(member, sortedPageable);

        cashLogResPage = Optional.of(cashLogResPage)
                .filter(Slice::hasContent)
                .orElse(Page.empty());

        return getMyCashLogResById(member, cashLogResPage);
    }

    public ConfirmResponse getConfirmRespById(final Long cashLogId, final Accessor accessor) {

        CashLog cashLog = findById(cashLogId);

        if (!cashLog.getMember().getId().equals(accessor.getMemberId())) throw new PaymentException(INVALID_AUTHORITY);

        Reservation reservation = reservationService.findByOrderIdAndMember(cashLog.getOrderId(), cashLog.getMember());

        Hotel hotel = reservation.getHotel();

        return ConfirmResponse.of(cashLog, reservation, hotel);
    }

    public CashLogIdResponse getCashLogIdById(final Long id) {
        return CashLogIdResponse.of(id);
    }

    private MyCashLogResponse getMyCashLogResById(final Member member, final Page<CashLogConfirmResponse> cashLogConfirmPage) {
        return MyCashLogResponse.of(member, cashLogConfirmPage);
    }
}
