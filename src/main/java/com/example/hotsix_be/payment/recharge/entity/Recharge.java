package com.example.hotsix_be.payment.recharge.entity;

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
public class Recharge extends CashLogMarker {

    // 입금자명
    private String depositor;

    // 가상계좌
    private String accountNumber;

    // 웹훅과 비교할 때 사용할 시크릿
    private String secret;

    private LocalDateTime cancelDate; // 충전 신청 취소 시

    public void cancelDone() {
        this.cancelDate = LocalDateTime.now();
    }

    public boolean isCancelled() {
        return this.cancelDate != null;
    }
}
