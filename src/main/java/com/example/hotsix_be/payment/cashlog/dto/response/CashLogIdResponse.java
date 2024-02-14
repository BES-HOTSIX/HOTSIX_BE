package com.example.hotsix_be.payment.cashlog.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "라우팅용 CashLogId 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class CashLogIdResponse {

    @Schema(description = "결제로 생성된 CashLog 의 id", example = "1")
    private final Long cashLogId;

    public static CashLogIdResponse of(final Long cashLogId) {
        return new CashLogIdResponse(cashLogId);
    }
}
