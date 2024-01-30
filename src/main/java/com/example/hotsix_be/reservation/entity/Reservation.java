package com.example.hotsix_be.reservation.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;


@Table(name = "reservations")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Reservation extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    private int guests;

    private long price;

    private boolean isPaid;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private Review review;

    public Reservation(
            final LocalDateTime checkInDate,
            final LocalDateTime checkOutDate,
            final int guests,
            final long price,
            final Hotel hotel,
            final boolean isPaid
    ) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.price = price;
        this.hotel = hotel;
        this.isPaid = isPaid;
    }

    public void updateCancelDate(LocalDateTime date) {
        this.cancelDate = date;
    }

    public void updateIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
