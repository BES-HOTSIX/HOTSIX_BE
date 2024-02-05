package com.example.hotsix_be.cashlog.service;

import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.dto.request.TossConfirmRequest;
import com.example.hotsix_be.cashlog.dto.response.*;
import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.exception.CashException;
import com.example.hotsix_be.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
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
    private final MemberService memberService;

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
                .orElseThrow(() -> new CashException(INVALID_REQUEST));
    }

    public ConfirmResponse getConfirmRespById(final Long id) {
        CashLog cashLog = findById(id).orElseThrow(() -> new CashException(INVALID_REQUEST));

        Reservation reservation = cashLog.getReservation();

        if (reservation == null) throw new CashException(INVALID_REQUEST);

        Hotel hotel = reservation.getHotel();

        return ConfirmResponse.of(cashLog, reservation, hotel);
    }

    public CashLogIdResponse getCashLogIdById(final Long id) {
        return CashLogIdResponse.of(id, null);
    }

    public CashLogIdResponse getCashLogIdById(final Long id, final TossPaymentResponse tossPaymentResponse) {
        return CashLogIdResponse.of(id, tossPaymentResponse);
    }

    public MyCashLogResponse getMyCashLogById(final Long id, PageImpl<CashLogConfirmResponse> cashLogConfirmPage) {
        Member member = memberService.getMemberById(id);

        return MyCashLogResponse.of(member, cashLogConfirmPage);
    }

    // 전반적인 입출금
    @Transactional
    public CashLog addCash(final Member member, final Long price, final String orderId, final Reservation reservation, final EventType eventType) {

        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                // reservation 이 null 일 경우 충전, null 이 아닐 경우 예약, 환불, 정산(예약취소)
                .reservation(reservation)
                .orderId(orderId)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        // 충전 혹은 차감 발생 후 member의 restCash 갱신
        Long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.updateRestCash(newRestCash);

        return cashLog;
    }

    // 무통장 입금
    @Transactional
    public CashLog addCash(final AddCashRequest addCashRequest, final EventType eventType) {
        Member member = memberService.getMemberByUsername(addCashRequest.getUsername());

        return addCash(
                member,
                addCashRequest.getPrice(),
                null,
                null,
                eventType
        );
    }

    // TODO 특정한 날짜에 한꺼번에 정산하는 기능 추가하기
    // 예치금 사용 결제
    @Transactional
    public CashLog payByCashOnly(final Reservation reservation) {
        Member buyer = reservation.getMember();
        Long payPrice = reservation.getPrice();

        Member owner = reservation.getHotel().getOwner();

        CashLog cashLog = addCash(buyer, payPrice * -1, null, reservation,EventType.사용__예치금_결제); // TODO UUID를 일반 결제에도 적용시켜야할까

        addCash(owner, payPrice, null, reservation, EventType.정산__예치금);

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

        addCash(buyer, pgPayPrice, orderId, reservation, EventType.충전__토스페이먼츠);
        CashLog cashLog = addCash(buyer, payPrice * -1, orderId, reservation, EventType.사용__예치금_결제);

        addCash(owner, payPrice, orderId, reservation, EventType.정산__예치금);

        reservation.payDone();

        return cashLog;
    }

    public boolean canPay(final Reservation reservation, final Long pgPayPrice) {
        Member member = reservation.getMember();
        Long restCash = member.getRestCash();
        Long price = reservation.getPrice();

        // 중복 결제 예방
        if (reservation.isPaid()) throw new CashException(INVALID_REQUEST);

        // 예치금이 충분한지 확인
        return price <= restCash + pgPayPrice;
    }

    public boolean canPay(final Long reservationId, final Long pgPayPrice) {
        Reservation reservation = reservationService.findUnpaidById(reservationId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        return canPay(reservation, pgPayPrice);
    }

//    @Transactional
//    public CashLog cancel(final Reservation reservation) {
//        reservationService
//    }
}
