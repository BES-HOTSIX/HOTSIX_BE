package com.example.hotsix_be.review.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {

    private Long id;
    private String body;
    private Double totalRating;

}