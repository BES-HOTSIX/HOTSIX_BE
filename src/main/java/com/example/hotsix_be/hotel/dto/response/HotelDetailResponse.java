package com.example.hotsix_be.hotel.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class HotelDetailResponse {

    private final Long id;
    private final String hotelType;
    private final String address;
    private final String addressDetail;
    private final Long roomCnt;
    private final Long bedCnt;
    private final Long bathroomCnt;
    private final Long maxPeople;
    private final List<String> facility;
    private final String nickname;
    private final String description;
    private final Long price;
    private final ImagesResponse imagesResponse;
    private final LocalDateTime createdAt;
    private final String host;

    public static HotelDetailResponse of(final Hotel hotel) {

        return new HotelDetailResponse(
                hotel.getId(),
                hotel.getHotelType(),
                hotel.getAddress(),
                hotel.getAddressDetail(),
                hotel.getRoomCnt(),
                hotel.getBedCnt(),
                hotel.getBathroomCnt(),
                hotel.getMaxPeople(),
                hotel.getFacility(),
                hotel.getNickname(),
                hotel.getDescription(),
                hotel.getPrice(),
                ImagesResponse.of(hotel.getImages()),
                hotel.getCreatedAt(),
                hotel.getOwner().getNickname()
        );
    }
}
