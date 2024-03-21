package com.example.hotsix_be.payment.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "토스페이먼츠 서버에서 받는 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class TossPaymentRequest {

    @Schema(description = "주문 고유 식별 코드", example = "o8sJILLP1EP6V1nLksCBL")
    private String orderId;

    @Schema(description = "결제 금액", example = "50000")
    private Long totalAmount;

    @Schema(description = "결제 수단", example = "간편결제")
    private String method;

    @Schema(description = "결제 상태", example = "DONE")
    private String status;

    @Schema(description = "가상계좌 시크릿키", example = "ps_E92LAa5PVbqlR7g5qzJJ37YmpXyJ")
    private String secret;

    @Schema(description = "은행코드", example = "20")
    private String bankCode;

    @Schema(description = "가상계좌번호", example = "X6516292118962")
    private VirtualAccountRequest virtualAccount;
}