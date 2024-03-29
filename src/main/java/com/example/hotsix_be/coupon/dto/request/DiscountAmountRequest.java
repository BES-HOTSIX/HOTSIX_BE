package com.example.hotsix_be.coupon.dto.request;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class DiscountAmountRequest {

    private Long discountAmount;

}
