package com.example.hotsix_be.payment.recharge.dto.response;

import com.example.hotsix_be.payment.recharge.entity.Recharge;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class RechargeConfirmResponse {

    private final LocalDateTime createdAt;

    private final String orderId;

    private final String virtualAccount;

    private final Long price;

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
