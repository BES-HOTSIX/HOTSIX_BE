package com.example.hotsix_be.review.dto.response;

import com.example.hotsix_be.review.entity.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberReviewResponseDTO {
    private final LocalDateTime createdAt;
    private final String body;
    private final Double totalRating;
    private final Long HotelId;

    public static MemberReviewResponseDTO of(final Review review) {
        return new MemberReviewResponseDTO(
                review.getCreatedAt(),
                review.getBody(),
                review.getTotalRating(),
                review.getHotel().getId()
        );
    }
}
