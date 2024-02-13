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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;

    private Long guests = 0L;

    private Long price = 0L;

    private boolean isPaid;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private Review review;

    public Reservation(
            final LocalDate checkInDate,
            final LocalDate checkOutDate,
            final Long guests,
            final Long price,
            final boolean isPaid,
            final Hotel hotel,
            final Member member
    ) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.price = price;
        this.isPaid = isPaid;
        this.hotel = hotel;
        this.member = member;
    }

    public void updateCancelDate(LocalDateTime date) {
        this.cancelDate = date;
    }

    public void updateIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public List<LocalDate> getReservedDateRange() {
        List<LocalDate> reservedDates = new ArrayList<>();

        if (this.checkOutDate.isBefore(LocalDate.now())) {
            return reservedDates;
        }

        LocalDate date = this.checkInDate;

        while (!date.isAfter(this.checkOutDate)) {
            reservedDates.add(date);
            date = date.plusDays(1);
        }

        return reservedDates;
    }

    public void payDone() {
        updateIsPaid(true);
    }
}
