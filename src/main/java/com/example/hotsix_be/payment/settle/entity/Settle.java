package com.example.hotsix_be.payment.settle.entity;

import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Settle extends CashLogMarker {
    // 은행코드
    private String bankCode;

    // 가상계좌
    private String accountNumber;

    // 수수료
    private Integer commission;

    // 실 지급액
    private Long actualAmount;

    // 정산 시작일
    private LocalDate startDate;

    // 정산 끝일
    private LocalDate endDate;
}
