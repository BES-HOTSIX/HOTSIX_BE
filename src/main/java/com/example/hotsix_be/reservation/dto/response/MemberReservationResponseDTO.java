package com.example.hotsix_be.reservation.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberReservationResponseDTO {
    private final Long id;
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
    private final boolean isPaid;
    private final Long hotelId;
    private final String buyerName;
    private final Long buyerRestCash;
    private final String buyerEmail;
    private final boolean hasMemberReviewedHotel;

    public static MemberReservationResponseDTO of(final Hotel hotel, final Reservation reservation, final Review review) {
        String imageUrl = "";
        if (!hotel.getImages().isEmpty()) {
            imageUrl = hotel.getImages().get(0).getUrl(); // 첫 번째 이미지의 URL을 가져옵니다.
        }

        return new MemberReservationResponseDTO(
                reservation.getId(),
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
                reservation.isPaid(),
                hotel.getId(),
                reservation.getMember().getUsername(),
                reservation.getMember().getRestCash(),
                reservation.getMember().getEmail(),
                review != null
        );
    }
}
