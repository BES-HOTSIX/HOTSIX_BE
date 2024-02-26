package com.example.hotsix_be.payment.cashlog.dto.response;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.payment.cashlog.entity.CashLog;
import com.example.hotsix_be.payment.cashlog.entity.EventType;
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

    @Schema(description = "주문 고유 식별 코드", example = "8sJILLP1EP6V1nLksCBL0")
    private final String orderId;

    @Schema(description = "CashLog 생성 일시", example = "2024-02-06 17:26:48.772390")
    private final LocalDateTime createdAt;

    public static CashLogConfirmResponse of(final CashLog cashLog) {
        Member member = cashLog.getMember();

        Long memberId = (member != null) ? member.getId() : null;

        return CashLogConfirmResponse.of(
                cashLog.getId(),
                cashLog.getEventType(),
                cashLog.getAmount(),
                memberId,
                cashLog.getOrderId(),
                cashLog.getCreatedAt()
        );
    }

    public static CashLogConfirmResponse of(
            final Long cashLogId,
            final EventType eventType,
            final Long price,
            final Long memberId,
            final String orderId,
            final LocalDateTime createdAt
    ) {
        return new CashLogConfirmResponse(
                cashLogId,
                eventType.toString(),
                price,
                memberId,
                orderId,
                createdAt
        );
    }
}
