package com.example.hotsix_be.review.dto.request;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    private String body;
    private Double amenities;
    private Double staffService;
    private Double cleanliness;

    private Long hotelId;
    private Long memberId;
    private Reservation reservation;
}