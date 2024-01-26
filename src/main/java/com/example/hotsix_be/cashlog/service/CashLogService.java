package com.example.hotsix_be.cashlog.service;

import com.example.hotsix_be.cashlog.dto.request.AddCashRequest;
import com.example.hotsix_be.cashlog.dto.response.ConfirmCashLogResponse;
import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.cashlog.entity.EventType;
import com.example.hotsix_be.cashlog.exception.CashException;
import com.example.hotsix_be.cashlog.repository.CashLogRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.hotsix_be.common.exception.ExceptionCode.INSUFFICIENT_DEPOSIT;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CashLogService {
    private final CashLogRepository cashLogRepository;
    private final ReservationService reservationService;

    public Optional<CashLog> findById(long id) {
        return cashLogRepository.findById(id);
    }

    public ConfirmCashLogResponse findRespById(long id) {
        CashLog cashLog = findById(id).orElse(null);

        if (cashLog == null) throw new CashException(INVALID_REQUEST);

        return ConfirmCashLogResponse.of(cashLog);
    }

    // 전반적인 입출금
    @Transactional
    public CashLog addCash(Member member, long price, Reservation reservation, EventType eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                // reservation 이 null 일 경우 충전 혹은 정산, null 이 아닐 경우 예약 혹은 환불(예약취소)
                .reservation(reservation)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        // 충전 혹은 차감 발생 후 member의 restCash 갱신
        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.toBuilder()
                .restCash(newRestCash)
                .build();

        return cashLog;
    }

    // 무통장 입금
    @Transactional
    public CashLog addCash(final AddCashRequest addCashRequest, EventType eventType) {
        return addCash(
                null,
                addCashRequest.getPrice(),
                null,
                eventType
        );
    }

    // 예치금 사용 결제
    @Transactional
    public CashLog payByCashOnly(Reservation reservation) {
        Member buyer = reservation.getMember();
        long restCash = buyer.getRestCash();
        long payPrice = reservation.getPrice();

        if (payPrice > restCash) throw new CashException(INSUFFICIENT_DEPOSIT);

        return addCash(
                buyer,
                reservation.getPrice() * -1,
                reservation,
                EventType.사용__예치금_결제
        );
    }

    // 복합 결제 및 토스페이먼츠 결제 // TODO payByTossPayments 엔드포인트 만들기
    @Transactional
    public void payByTossPayments(long reservationId, long pgPayPrice) {
        Reservation reservation = reservationService.findOpById(reservationId).orElse(null);

        if (reservation == null) throw new CashException(INVALID_REQUEST);

        Member buyer = reservation.getMember();
        long restCash = buyer.getRestCash();
        long payPrice = reservation.getPrice();

        long useCash = payPrice - pgPayPrice;
        addCash(buyer, pgPayPrice, reservation, EventType.충전__토스페이먼츠);
        addCash(buyer, pgPayPrice * -1, reservation, EventType.사용__예치금_결제);

        if (useCash > 0) {
            if (useCash > restCash) {
                throw new CashException(INSUFFICIENT_DEPOSIT);
            }

            addCash(buyer, useCash * -1, reservation, EventType.사용__예치금_결제);
        }

        reservationService.payDone(reservation);
    }


    public boolean canPay(Reservation reservation, long pgPayPrice) {
        Member member = reservation.getMember();
        long restCash = member.getRestCash();
        long price = reservation.getPrice();

        // 중복 결제 예방
        if (reservation.isPaid()) throw new CashException(INVALID_REQUEST);

        return price <= restCash + pgPayPrice;
    }

    // orderId = order.getCreateDate() + "__" + order.getId();
    public boolean canPay(String orderId, long pgPayPrice) {
        Reservation reservation = reservationService.findOpById(Long.parseLong(orderId.split("__", 2)[1])).orElse(null);

        // 잘못된 방식의 접근으로 생성된 예약 객체가 없을 경우
        if (reservation == null) throw new CashException(INVALID_REQUEST);

        return canPay(reservation, pgPayPrice);
    }
}
