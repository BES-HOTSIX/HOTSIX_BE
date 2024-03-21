package com.example.hotsix_be.payment.recharge.dto.response;

import com.example.hotsix_be.payment.recharge.entity.Recharge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "Recharge 엔티티 내용 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class RechargeConfirmResponse {

    @Schema(description = "Recharge 생성 일시", example = "2024-02-06 17:26:48.772390")
    private final LocalDateTime createdAt;

    @Schema(description = "주문 고유 식별 코드", example = "o8sJILLP1EP6V1nLksCBL")
    private final String orderId;

    @Schema(description = "가상계좌번호", example = "12312345612345")
    private final String virtualAccount;

    @Schema(description = "충전 금액", example = "100000")
    private final Long price;

    @Schema(description = "결제 상태", example = "입금 대기")
    private final String status;

    public static RechargeConfirmResponse of(final Recharge recharge) {
        String status = "입금 대기";
        if (recharge.isPaid()) status = "입금 완료";
        if (recharge.isCancelled()) status = "입금 취소";

        return new RechargeConfirmResponse(
                recharge.getCreatedAt(),
                recharge.getOrderId(),
                recharge.getAccountNumber(),
                recharge.getAmount(),
                status
        );
    }
}
