package com.example.hotsix_be.review.dto.response;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PRIVATE;

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


    public static ReviewResponseDTO of(final Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getMember(),
                review.getBody(),
                review.getAmenities(),
                review.getStaffService(),
                review.getCleanliness(),
                review.getRating()
        );
    }
}