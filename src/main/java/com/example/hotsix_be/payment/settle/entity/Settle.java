package com.example.hotsix_be.payment.settle.entity;

import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Settle extends CashLogMarker {
//    // 은행코드
//    private String bankCode;
//
//    // 가상계좌
//    private String accountNumber;

    // 수수료율
    private Integer commissionRate;

    // 수수료
    private Long commission;

    // 총 금액
    private Long totalAmount;
}
