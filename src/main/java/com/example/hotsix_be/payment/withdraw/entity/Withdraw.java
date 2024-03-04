package com.example.hotsix_be.payment.withdraw.entity;


import com.example.hotsix_be.payment.cashlog.entity.CashLogMarker;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Withdraw extends CashLogMarker {
    // 은행 코드
    private String bankCode;

    // 환전 계좌w
    private String accountNumber;

    // 취소일자 (취소되지 않았을 경우 null)
    private LocalDateTime cancelDate;
}
