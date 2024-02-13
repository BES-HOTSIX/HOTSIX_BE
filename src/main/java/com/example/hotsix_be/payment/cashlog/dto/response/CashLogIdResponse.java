package com.example.hotsix_be.payment.cashlog.dto.response;

import com.example.hotsix_be.payment.payment.dto.response.TossEasyPayResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogIdResponse {

    @Schema(description = "결제로 생성된 CashLog 의 id", example = "1")
    private final Long cashLogId;

    @Schema(description = "토스페이먼츠")
    private final TossEasyPayResponse tossEasyPayResponse;

    public static CashLogIdResponse of(final Long cashLogId) {
        return CashLogIdResponse.of(cashLogId, null);
    }

    public static CashLogIdResponse of(
            final Long cashLogId,
            final TossEasyPayResponse tossEasyPayResponse
    ) {
        return new CashLogIdResponse(
                cashLogId,
                tossEasyPayResponse
        );
    }
}
