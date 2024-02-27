package com.example.hotsix_be.payment.cashlog.service;


import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
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
import com.example.hotsix_be.reservation.exception.ReservationException;
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

    @Transactional
    public <T extends CashLogMarker> void initializeCashLog(final T cashLogMarker, final CashLog cashLog) {
        if (cashLogMarker.isInitialized()) throw new PaymentException(ALREADY_BEEN_INITIALIZED);

        cashLogMarker.updateCashLog(cashLog);
    }

    // 전반적인 입출금
    @Transactional
    public <T extends CashLogMarker> T addCash(
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

        initializeCashLog(cashLogMarker, cashLog);

        return cashLogMarker;
    }

    // 결제 마무리
    @Transactional
    public <T extends CashLogMarker> void addCashDone(final T cashLogMarker) {
        Member member = cashLogMarker.getMember();

        // 금액 이동
        Long newRestCash = member.getRestCash() + cashLogMarker.getAmount();
        member.updateRestCash(newRestCash);

        cashLogMarker.payDone();
    }

    public Optional<CashLog> findById(final Long id) {
        return cashLogRepository.findById(id);
    }

    // 개인 캐시 사용 내역 페이지의 cashLog 리스트
    public Page<CashLogConfirmResponse> findMyPageList(final Member member, final Pageable pageable) {

        Pageable sortedPageable = ((PageRequest) pageable).withSort(Sort.by("createdAt").descending());

        Page<CashLogConfirmResponse> cashLogResPage = cashLogRepository.getCashLogConfirmResForPayByMember(member, sortedPageable);

        return Optional.of(cashLogResPage)
                .filter(Slice::hasContent)
                .orElse(Page.empty());
    }

    public ConfirmResponse getConfirmRespById(final Long id) {
        CashLog cashLog = findById(id).orElseThrow(() -> new PaymentException(INVALID_REQUEST));

        Reservation reservation = reservationService.findByOrderId(cashLog.getOrderId())
                .orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        Hotel hotel = reservation.getHotel();

        return ConfirmResponse.of(cashLog, reservation, hotel);
    }

    public CashLogIdResponse getCashLogIdById(final Long id) {
        return CashLogIdResponse.of(id);
    }

    public MyCashLogResponse getMyCashLogById(final Member member, final Page<CashLogConfirmResponse> cashLogConfirmPage) {
        return MyCashLogResponse.of(member, cashLogConfirmPage);
    }
}
