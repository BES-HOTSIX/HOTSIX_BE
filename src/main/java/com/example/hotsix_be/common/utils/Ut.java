package com.example.hotsix_be.common.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class Ut {
    public static LocalDate getSettleDate() {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    }
}
