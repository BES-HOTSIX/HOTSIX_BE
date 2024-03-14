package com.example.hotsix_be.payment.settle.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class SettleUt {
    @Getter
    @Value("${settle.commissionRate}")
    private static Integer commissionRate;

    public static LocalDate getSettleDate() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    }

    public static Long calculateCommission(Long price) {
        return (long) Math.floor((double) price / commissionRate);
    }
}
