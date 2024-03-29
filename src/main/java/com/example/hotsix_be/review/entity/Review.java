package com.example.hotsix_be.review.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
@Entity
public class Review extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "리뷰 내용을 입력해주세요")
    private String body;

    private Double rating;

    @NotNull(message = "별점을 입력해주세요")
    private Double amenities;

    @NotNull(message = "별점을 입력해주세요")
    private Double staffService;

    @NotNull(message = "별점을 입력해주세요")
    private Double cleanliness;

    private Double totalAmenities;

    private Double totalCleanliness;

    private Double totalStaffService;

    private Double totalRating;
    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @JsonIgnore
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    public Review(
            final String body,
            final Double amenities,
            final Double staffService,
            final Double cleanliness,
            final Double rating,
            final Hotel hotel,
            final Reservation reservation,
            final Member member
    ) {
        this.body = body;
        this.amenities = amenities;
        this.staffService = staffService;
        this.cleanliness = cleanliness;
        this.rating = rating;
        this.hotel = hotel;
        this.reservation = reservation;
        this.member = member;
    }

    public void update(final ReviewRequestDTO reviewRequestDTO) {
        this.body = reviewRequestDTO.getBody();
        this.rating = reviewRequestDTO.getRating();
        this.amenities = reviewRequestDTO.getAmenities();
        this.staffService = reviewRequestDTO.getStaffService();
        this.cleanliness = reviewRequestDTO.getCleanliness();
    }

    public void updateAverages(double totalRating, double totalAmenities, double totalStaffService, double totalCleanliness) {
        this.totalRating = totalRating;
        this.totalAmenities = totalAmenities;
        this.totalStaffService = totalStaffService;
        this.totalCleanliness = totalCleanliness;
    }
}