package com.example.hotsix_be.review.dto.response;
import com.example.hotsix_be.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ReviewResponseDTO {

    private final Long id;
    private final Member member;
    private final String body;
    private final Double amenities;
    private final Double staffService;
    private final Double cleanliness;
    private final Double rating;


    public ReviewResponseDTO(Long id, Member member, String body, Double amenities, Double staffService, Double cleanliness, Double rating) {
        this.id = id;
        this.member = member;
        this.body = body;
        this.amenities = amenities;
        this.staffService = staffService;
        this.cleanliness = cleanliness;
        this.rating = rating;
    }

}