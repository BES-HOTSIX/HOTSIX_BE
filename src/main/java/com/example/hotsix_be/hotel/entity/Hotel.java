package com.example.hotsix_be.hotel.entity;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

import com.example.hotsix_be.common.entity.DateEntity;
import com.example.hotsix_be.hotel.dto.request.HotelModifyRequest;
import com.example.hotsix_be.image.entity.Image;
import com.example.hotsix_be.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private Long bathroomCnt;

    private Long maxPeople;

    @ElementCollection
    private List<String> facility = new ArrayList<>();

    private String nickname;

    private String description;

    private Long price;

    @OneToMany(mappedBy = "hotel", cascade = {REMOVE, PERSIST}, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();


    @JsonIgnore
    @ManyToOne(cascade = {PERSIST, REMOVE})
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
            final Long price
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
    }

    public void update(final HotelModifyRequest hotelModifyRequest){
        this.hotelType = hotelModifyRequest.getHotelType();
        this.address = hotelModifyRequest.getAddress();
        this.addressDetail = hotelModifyRequest.getAddressDetail();
        this.roomCnt = hotelModifyRequest.getRoomCnt();
        this.bedCnt = hotelModifyRequest.getBedCnt();
        this.bathroomCnt = hotelModifyRequest.getBathroomCnt();
        this.maxPeople = hotelModifyRequest.getMaxPeople();
        this.facility = hotelModifyRequest.getFacility();
        this.nickname = hotelModifyRequest.getNickname();
        this.description = hotelModifyRequest.getDescription();
        this.price = hotelModifyRequest.getPrice();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setHotel(this);
    }

}
