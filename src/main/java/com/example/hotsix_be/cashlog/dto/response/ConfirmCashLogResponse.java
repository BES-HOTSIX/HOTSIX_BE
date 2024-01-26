package com.example.hotsix_be.cashlog.dto.response;

import com.example.hotsix_be.cashlog.entity.CashLog;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.withdrawapply.entity.WithdrawApply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ConfirmCashLogResponse {
    private final String eventType;
    private final Long price;
    private final Long memberId;
    private final Long reservationId;
    private final Long withdrawApplyId;

    public static ConfirmCashLogResponse of(final CashLog cashLog) {
        Reservation reservation = cashLog.getReservation();
        WithdrawApply withdrawApply = cashLog.getWithdrawApply();
        Member member = cashLog.getMember();

        Long reservationId = reservation != null ? reservation.getId() : null;
        Long withdrawApplyId = withdrawApply != null ? withdrawApply.getId() : null;
        Long memberId = member != null ? member.getId() : null;

        return new ConfirmCashLogResponse(
                cashLog.getEventType().toString(),
                cashLog.getPrice(),
                memberId,
                reservationId,
                withdrawApplyId
        );
    }
}
