package com.example.hotsix_be.review.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {

    private final String body;
    private final Double amenities;
    private final Double staffService;
    private final Double cleanliness;
    private final Double rating;


    public ReviewResponseDTO(String body, Double amenities, Double staffService, Double cleanliness, Double rating) {

        this.body = body;
        this.amenities = amenities;
        this.staffService = staffService;
        this.cleanliness = cleanliness;
        this.rating = rating;
    }

}