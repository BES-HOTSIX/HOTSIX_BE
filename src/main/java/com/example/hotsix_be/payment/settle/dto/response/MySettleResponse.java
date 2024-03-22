package com.example.hotsix_be.payment.settle.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "Settle 정산 정보 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MySettleResponse {

    @Schema(description = "보유 캐시", example = "100000")
    private final Long restCash;

    @Schema(description = "다음 정산 예정일", example = "2024-01-01")
    private final LocalDate settleDate;

    @Schema(description = "정산 예정 금액", example = "100000")
    private final Long expectedTotalSettleAmount;


    public static MySettleResponse of(
            final Long restCash,
            final LocalDate settleDate,
            final Long expectedTotalSettleAmount
    ) {
        return new MySettleResponse(
                restCash,
                settleDate,
                expectedTotalSettleAmount
        );
    }
}
