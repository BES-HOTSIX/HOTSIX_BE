package com.example.hotsix_be.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class ReviewRequestDTO {

    private String body;

    private Double amenities;

    private Double staffService;

    private Double cleanliness;

    private Double rating;

    //private Long hotelId;


    public Double getRating() {
        this.rating = (this.amenities + this.staffService + this.cleanliness) / 3.0;
        return Math.round(this.rating * 100.0) / 100.0;
    }

}