package com.example.hotsix_be.review.entity;
import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
@Entity
public class Review extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    private Double rating;

    private Double amenities;

    private Double staffService;
    private Double cleanliness;
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


    @Builder
    public Review(String body, Double amenities, Double staffService, Double cleanliness, Double rating, Hotel hotel, Member member, Reservation reservation) {
        this.body = body;
        this.amenities = amenities;
        this.staffService = staffService;
        this.cleanliness = cleanliness;
        this.rating = rating;
        this.hotel = hotel;
        this.member = member;
        this.reservation = reservation;
    }
    }