package com.example.hotsix_be.payment.payment.dto.request;

import com.example.hotsix_be.coupon.entity.CouponType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Schema(description = "토스페이먼츠 서버 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class TossConfirmRequest {

    @Schema(description = "결제 타입", example = "NORMAL")
    private String paymentType;

    @Schema(description = "주문 고유 식별 코드", example = "a4CWyWY5m89PNh7xJwhk1")
    private String orderId;

    @Schema(description = "주문 금액", example = "100000")
    private String amount;

    @Schema(description = "결제 키값", example = "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6")
    private String paymentKey;

    private Long discountAmount;

    private CouponType couponType;

}
