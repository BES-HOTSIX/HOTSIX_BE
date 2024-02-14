package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservedDatesOfHotelResponse {
    private final List<LocalDate> reservedDates;

    public static ReservedDatesOfHotelResponse of(final List<Reservation> reservations) {
        List<LocalDate> allReservedDates = new ArrayList<>();
        // 모든 예약으로부터 예약된 날짜 범위를 추출
        for (Reservation reservation : reservations) {
            allReservedDates.addAll(reservation.getReservedDateRange());
        }
        // 날짜 목록 정렬 및 중복 제거
        allReservedDates = allReservedDates.stream().distinct().sorted().collect(Collectors.toList());

        List<LocalDate> additionalDates = new ArrayList<>();
        // 연속되지 않는 날짜 간격 식별 및 하루 빠진 날짜 추가
        for (int i = 0; i < allReservedDates.size() - 1; i++) {
            LocalDate current = allReservedDates.get(i);
            LocalDate next = allReservedDates.get(i + 1);
            // 다음 날짜와의 간격이 딱 하루인 경우 해당 하루를 추가
            if (current.plusDays(2).equals(next)) {
                additionalDates.add(current.plusDays(1));
            }
        }

        // 추가된 하루만 빠진 날짜들을 reservedDates에 포함
        List<LocalDate> updatedReservedDates = Stream.concat(allReservedDates.stream(), additionalDates.stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        return new ReservedDatesOfHotelResponse(updatedReservedDates);
    }
}
