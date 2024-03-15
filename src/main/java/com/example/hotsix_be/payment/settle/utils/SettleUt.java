package com.example.hotsix_be.payment.settle.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class SettleUt {
    @Getter
    private static Integer commissionRate;

    public SettleUt(@Value("${settle.commissionRate}") final Integer commissionRate) {
        this.commissionRate = commissionRate;
    }

    public static LocalDate getBaseSettleDate() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    }

    public static LocalDate getExpectedSettleDate() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
    }

    public static Long calculateCommission(Long price) {
        return (long) Math.floor((double) price / commissionRate);
    }
}
