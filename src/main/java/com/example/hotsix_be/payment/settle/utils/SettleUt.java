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

    @Value("${settle.commissionRate}")
    private void setCommissionRate(final Integer commissionRate) {
        this.commissionRate = commissionRate;
    }

    public static LocalDate getBaseSettleDate() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    }

    public static LocalDate getExpectedSettleDate(LocalDate checkOutDate) { // 체크아웃 요일 다음주의 수요일 반환
        LocalDate currentDateTime = LocalDate.now();

        LocalDate lastSunday = currentDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

        if ( checkOutDate.isBefore(lastSunday)) return getExpectedSettleDate();

        return currentDateTime.plusWeeks(1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
    }

    public static LocalDate getExpectedSettleDate() { // 오는 수요일 반환
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
    }

    public static Long calculateCommission(Long price) {
        return (long) Math.floor((double) price / commissionRate);
    }
}
