package com.example.hotsix_be.review.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ReviewResponseDTO {

    private final Long id;
    private final Member member;
    private final String body;
    private final Double amenities;
    private final Double staffService;
    private final Double cleanliness;
    private final Double rating;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Double totalAmenities;
    private final Double totalCleanliness;
    private final Double totalStaffService;
    private final Double totalRating;


    public static ReviewResponseDTO of(final Review review, final ReviewSummaryResponse summary) {
        return new ReviewResponseDTO (
                review.getId(),
                review.getMember(),
                review.getBody(),
                review.getAmenities(),
                review.getStaffService(),
                review.getCleanliness(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                summary.getTotalAmenities(),
                summary.getTotalCleanliness(),
                summary.getTotalStaffService(),
                summary.getTotalRating()
        );
    }
}