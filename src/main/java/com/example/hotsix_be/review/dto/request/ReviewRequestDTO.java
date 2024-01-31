package com.example.hotsix_be.review.dto.request;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.review.entity.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    private final String body;
    private final Double amenities;
    private final Double staffService;
    private final Double cleanliness;
    private final Double rating;

    private final Hotel hotel;
    private final Member member;
    private final Reservation reservation;

    public ReviewRequestDTO(String body, Double amenities, Double staffService, Double cleanliness, Double rating, Hotel hotel, Member member, Reservation reservation) {
        this.body = body;
        this.amenities = amenities;
        this.staffService = staffService;
        this.cleanliness = cleanliness;
        this.rating = rating;
        this.hotel = hotel;
        this.member = member;
        this.reservation = reservation;
    }

    public Double getRating() {
        double rating = (amenities + staffService + cleanliness) / 3;
        return Math.round(rating * 100.0) / 100.0;
    }
}