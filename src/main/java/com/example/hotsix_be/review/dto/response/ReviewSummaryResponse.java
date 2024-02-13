package com.example.hotsix_be.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewSummaryResponse {
    private final double totalAmenities;
    private final double totalCleanliness;
    private final double totalStaffService;
    private final double totalRating;
}