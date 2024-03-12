package com.example.hotsix_be.reservation.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HostReservationSummaryResponse {

    private Page<HostReservationPageResponse> reservations;
    private Long totalSales;
    private Long completedReservationCount;

}
