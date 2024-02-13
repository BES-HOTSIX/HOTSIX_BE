package com.example.hotsix_be.payment.cashlog.service;


import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.CashLogIdResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.ConfirmResponse;
import com.example.hotsix_be.payment.cashlog.dto.response.MyCashLogResponse;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
import com.example.hotsix_be.payment.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.payment.payment.dto.request.TossConfirmRequest;
import com.example.hotsix_be.payment.payment.exception.PaymentException;
import com.example.hotsix_be.payment.recharge.entity.Recharge;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.aventrix.jnanoid.jnanoid.NanoIdUtils.randomNanoId;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_RESERVATION_ID;
import static com.example.hotsix_be.payment.cashlog.entity.EventType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CashLogService {
    private final CashLogRepository cashLogRepository;
    private final ReservationService reservationService;
    private final MemberService memberService;

    @Transactional // Recharge 생성 addCash
    public CashLog addCash(final Member member, final Long price, final Recharge recharge) {
        return addCash(member, price, recharge.getOrderId(), null, recharge, recharge.getEventType());
    }

    @Transactional // Reservation 계산용 addCash
    public CashLog addCash(final Member member, final Long price, final Reservation reservation, final EventType eventType) {
        return addCash(member, price, reservation.getOrderId(), reservation, null, eventType);
    }

    // 전반적인 입출금
    @Transactional
    public CashLog addCash(final Member member, final Long price, final String orderId, final Reservation reservation, final Recharge recharge, final EventType eventType) {

        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .orderId(orderId)
                // reservation 이 null 일 경우 충전, null 이 아닐 경우 예약, 환불, 정산(예약취소)
                .reservation(reservation)
                .recharge(recharge)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        // 충전 혹은 차감 발생 후 member 의 restCash 갱신
        Long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.updateRestCash(newRestCash);

        return cashLog;
    }

    public Optional<CashLog> findById(final Long id) {
        return cashLogRepository.findById(id);
    }

    // 개인 캐시 사용 내역 페이지의 cashLog 리스트
    public Page<CashLog> findMyPageList(final Long memberId, final Pageable pageable) {
        Member member = memberService.getMemberById(memberId);

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<CashLog> pageCashLog = cashLogRepository.findAllByMember(member, sortedPageable);

        return Optional.of(pageCashLog)
                .filter(Slice::hasContent)
                .orElseThrow(() -> new PaymentException(INVALID_REQUEST));
    }

    public ConfirmResponse getConfirmRespById(final Long id) {
        CashLog cashLog = findById(id).orElseThrow(() -> new PaymentException(INVALID_REQUEST));

        Reservation reservation = cashLog.getReservation();

        if (reservation == null) throw new PaymentException(INVALID_REQUEST);

        Hotel hotel = reservation.getHotel();

        return ConfirmResponse.of(cashLog, reservation, hotel);
    }

    public CashLogIdResponse getCashLogIdById(final Long id) {
        return CashLogIdResponse.of(id);
    }

    public MyCashLogResponse getMyCashLogById(final Long id, final PageImpl<CashLogConfirmResponse> cashLogConfirmPage) {
        Member member = memberService.getMemberById(id);

        return MyCashLogResponse.of(member, cashLogConfirmPage);
    }

    // TODO 특정한 날짜에 한꺼번에 정산하는 기능 추가하기
    // 예치금 사용 결제
    @Transactional
    public CashLog payByCashOnly(final Reservation reservation) {
        Member buyer = reservation.getMember();
        Long payPrice = reservation.getPrice();
        Member owner = reservation.getHotel().getOwner();

        reservation.updateOrderId(randomNanoId());

        CashLog cashLog = addCash(buyer, payPrice * -1, reservation, 결제__예치금);

        addCash(owner, payPrice, reservation, EventType.정산__예치금);

        reservation.payDone();

        return cashLog;
    }

    // 복합 결제 및 토스페이먼츠 결제
    @Transactional
    public CashLog payByTossPayments(
            final TossConfirmRequest tossConfirmRequest,
            final Reservation reservation
    ) {
        Member buyer = reservation.getMember();
        Member owner = reservation.getHotel().getOwner();
        Long pgPayPrice = Long.valueOf(tossConfirmRequest.getAmount());
        Long payPrice = reservation.getPrice();
        String orderId = tossConfirmRequest.getOrderId();

        // orderId 입력
        reservation.updateOrderId(orderId);

        addCash(buyer, pgPayPrice, reservation, 충전__토스페이먼츠);

        CashLog cashLog = addCash(buyer, payPrice * -1, reservation, 결제__예치금);

        addCash(owner, payPrice, reservation, EventType.정산__예치금);

        // 결제 완료 처리
        reservation.payDone();

        return cashLog;
    }

    @Transactional
    public CashLog cancelReservation(final Reservation reservation) {
        reservation.cancelDone();

        addCash(reservation.getHotel().getOwner(), reservation.getPrice() * -1, reservation, 정산__예약취소);

        return addCash(reservation.getMember(), reservation.getPrice(), reservation, 취소__예치금);
    }

    public boolean canPay(final Long reservationId, final Long pgPayPrice) {
        Reservation reservation = reservationService.findUnpaidById(reservationId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        return canPay(reservation, pgPayPrice);
    }

    public boolean canPay(final Reservation reservation, final Long pgPayPrice) {
        Member member = reservation.getMember();
        Long restCash = member.getRestCash();
        Long price = reservation.getPrice();

        // 중복 결제 예방
        if (reservation.isPaid()) throw new PaymentException(INVALID_REQUEST);

        // 예치금이 충분한지 확인
        return price <= restCash + pgPayPrice;
    }
}
