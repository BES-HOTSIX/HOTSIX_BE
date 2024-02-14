package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReservationInfoResponse {
    private final String hotelNickname;
    private final String hotelDescription;
    private final String hotelPhotoUrl;
    private final String hotelHost;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime cancelDate;
    private final Long numOfGuests;
    private final Long paidPrice;
    private final Long hotelId;
    private final Long reviewId;

    public static ReservationInfoResponse of(final Hotel hotel, final Reservation reservation, final Review review) {
        String imageUrl = "";
        if (!hotel.getImages().isEmpty()) {
            imageUrl = hotel.getImages().get(0).getUrl(); // 첫 번째 이미지의 URL을 가져옵니다.
        }

        Long reviewId = Optional.ofNullable(review)
                .map(Review::getId)
                .orElse(0L);

        return new ReservationInfoResponse(
                hotel.getNickname(),
                hotel.getDescription(),
                imageUrl,
                hotel.getOwner().getUsername(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getCreatedAt(),
                reservation.getCancelDate(),
                reservation.getGuests(),
                reservation.getPrice(),
                hotel.getId(),
                reviewId
        );
    }
}
