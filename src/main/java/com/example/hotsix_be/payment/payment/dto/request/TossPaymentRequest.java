package com.example.hotsix_be.payment.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
@ToString
public class TossPaymentRequest {

    @Schema(description = "주문 고유 식별 코드", example = "8sJILLP1EP6V1nLksCBL0")
    private String orderId;

    @Schema(description = "토스페이먼츠 결제 금액", example = "50000")
    private Long totalAmount;

    // TODO 임시 필드
    private String method;

    // TODO 임시 필드
    private String status;

    // TODO 임시 필드
    private String secret;

    // TODO 임시 필드
    private VirtualAccountRequest virtualAccount;
}