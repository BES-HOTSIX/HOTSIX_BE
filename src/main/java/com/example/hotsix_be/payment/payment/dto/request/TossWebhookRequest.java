package com.example.hotsix_be.payment.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "토스페이먼츠 가상계좌 입금 확인 웹훅 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class TossWebhookRequest {

    @Schema(description = "주문 고유 식별 코드", example = "8sJILLP1EP6V1nLksCBL0")
    private String orderId;

    @Schema(description = "결제 상태", example = "DONE")
    private String status;

    @Schema(description = "가상계좌 시크릿키", example = "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ")
    private String secret;
}
