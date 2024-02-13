package com.example.hotsix_be.hotel.entity;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.hotel.dto.request.HotelUpdateRequest;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.bind.annotation.DeleteMapping;

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

    private Long bathroomCnt;

    private Long maxPeople;

    @ElementCollection
    @CollectionTable(
            name = "facility",
            joinColumns = @JoinColumn(name = "hotel_id"),
            foreignKey = @ForeignKey(
                    name = "fk_hotel_facility",
                    foreignKeyDefinition = "foreign key (hotel_id) references hotels (id) on delete cascade"
            )
    )

    private List<String> facility = new ArrayList<>();

    private String nickname;

    private String description;

    private Long price;

    @Builder.Default
    private int likesCount = 0;

    @OneToMany(mappedBy = "hotel", cascade = {REMOVE, PERSIST}, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", cascade = { REMOVE }, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    public Hotel(
            final String hotelType,
            final String address,
            final String addressDetail,
            final Long roomCnt,
            final Long bedCnt,
            final Long bathroomCnt,
            final Long maxPeople,
            final List<String> facility,
            final String nickname,
            final String description,
            final Long price,
            final Member owner
    ) {
        this.hotelType = hotelType;
        this.address = address;
        this.addressDetail = addressDetail;
        this.roomCnt = roomCnt;
        this.bedCnt = bedCnt;
        this.bathroomCnt = bathroomCnt;
        this.maxPeople = maxPeople;
        this.facility = facility;
        this.nickname = nickname;
        this.description = description;
        this.price = price;
        this.owner = owner;
    }

    public void update(final HotelUpdateRequest hotelUpdateRequest) {
        this.hotelType = hotelUpdateRequest.getHotelType();
        this.address = hotelUpdateRequest.getAddress();
        this.addressDetail = hotelUpdateRequest.getAddressDetail();
        this.roomCnt = hotelUpdateRequest.getRoomCnt();
        this.bedCnt = hotelUpdateRequest.getBedCnt();
        this.bathroomCnt = hotelUpdateRequest.getBathroomCnt();
        this.maxPeople = hotelUpdateRequest.getMaxPeople();
        this.facility = hotelUpdateRequest.getFacility();
        this.nickname = hotelUpdateRequest.getNickname();
        this.description = hotelUpdateRequest.getDescription();
        this.price = hotelUpdateRequest.getPrice();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setHotel(this);
    }

    public void incrementLikesCount() {
        this.likesCount++;
    }

    public void decrementLikesCount() {
        this.likesCount--;
    }
}
