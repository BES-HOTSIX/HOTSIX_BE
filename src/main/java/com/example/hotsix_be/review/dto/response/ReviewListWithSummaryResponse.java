package com.example.hotsix_be.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewListWithSummaryResponse {
    private final List<ReviewResponseDTO> reviews;
    private final ReviewSummaryResponse summary;
}