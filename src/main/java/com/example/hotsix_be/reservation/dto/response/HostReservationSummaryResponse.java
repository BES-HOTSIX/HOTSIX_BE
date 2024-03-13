package com.example.hotsix_be.reservation.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HostReservationSummaryResponse {

    private final Page<HostReservationPageResponse> reservations;
    private final Long totalSales;
    private final Long completedReservationCount;

    public static HostReservationSummaryResponse of(final Page<HostReservationPageResponse> reservations, final Long totalSales, final Long completedReservationCount) {
        return new HostReservationSummaryResponse(reservations, totalSales, completedReservationCount);
    }

}
