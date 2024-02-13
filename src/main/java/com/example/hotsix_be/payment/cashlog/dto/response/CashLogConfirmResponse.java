package com.example.hotsix_be.payment.cashlog.dto.response;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "CashLog 엔티티 내용 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogConfirmResponse {

    @Schema(description = "CashLog 아이디", example = "1")
    private final Long cashLogId;

    @Schema(description = "입출금 유형", example = "결제")
    private final String eventType;

    @Schema(description = "상품 가격", example = "100000")
    private final Long price;

    @Schema(description = "해당 CashLog 를 가진 유저 아이디", example = "1")
    private final Long memberId;

    @Schema(description = "예약 아이디", example = "1")
    private final Long reservationId;
    private final Long withdrawApplyId;
    private final String orderId;
    private final LocalDateTime createdAt;

    public static CashLogConfirmResponse of(final CashLog cashLog) {
        Reservation reservation = cashLog.getReservation();
        Member member = cashLog.getMember();

        Long reservationId = (reservation != null) ? reservation.getId() : null;
        Long memberId = (member != null) ? member.getId() : null;

        return new CashLogConfirmResponse(
                cashLog.getId(),
                cashLog.getEventType().toString(),
                cashLog.getPrice(),
                memberId,
                reservationId,
                withdrawApplyId,
                cashLog.getOrderId(),
                cashLog.getCreatedAt()
        );
    }
}
