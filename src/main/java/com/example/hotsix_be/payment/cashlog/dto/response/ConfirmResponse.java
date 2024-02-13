package com.example.hotsix_be.payment.cashlog.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "예약 결제, 취소 확인 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ConfirmResponse {

    @Schema(description = "예약을 취소했거나 결제한 사람의 유저 아이디", example = "1")
    private final Long memberId;

    @Schema(description = "해당 예약 아이디", example = "1")
    private final Long reserveId;

    @Schema(description = "주문 고유 식별 코드", example = "8sJILLP1EP6V1nLksCBL0")
    private final String orderId;

    @Schema(description = "숙소의 이름", example = "땡땡호텔")
    private final String hotelNickname;

    @Schema(description = "예약 가격", example = "100000")
    private final Long price;

    @Schema(description = "체크인 날짜", example = "2024-01-28")
    private final LocalDate checkInDate;

    @Schema(description = "체크아웃 날짜", example = "2024-01-30")
    private final LocalDate checkOutDate;

    @Schema(description = "입출금 유형", example = "결제")
    private final String eventType;

    public static ConfirmResponse of(
            final CashLog cashLog,
            final Reservation reservation,
            final Hotel hotel
    ) {
        Long price = cashLog.getPrice();
        if (price < 0) price *= -1;

        return new ConfirmResponse(
                reservation.getMember().getId(),
                reservation.getId(),
                reservation.getOrderId(),
                hotel.getNickname(),
                price,
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                cashLog.getEventType().getStatus()
        );
    }
}
