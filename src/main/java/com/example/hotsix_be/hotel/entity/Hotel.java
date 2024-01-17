package com.example.hotsix_be.hotel.entity;

import com.example.hotsix_be.common.entity.DateEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hotels")
@Entity
public class Hotel extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelType;

    private String address;

    private String addressDetail;

    private Long roomCnt;

    private Long bedCnt;

    private Long maxPeople;

    private String facility;

    private String nickname;

    private String description;

    private Long price;

    @ElementCollection
    @CollectionTable(name = "unavailable_dates", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "date")
    private Set<LocalDate> unavailableDates;

}
