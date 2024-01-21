package com.example.hotsix_be.hotel.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.example.hotsix_be.hotel.entity.Hotel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private final Long maxPeople;
    private final List<String> facility;
    private final String nickname;
    private final String description;
    private final Long price;
    private final ImagesResponse imagesResponse;
    private final Set<LocalDate> unavailableDates;
    private final LocalDateTime createdAt;


    public static HotelDetailResponse of(final Hotel hotel) {

        return new HotelDetailResponse(
                hotel.getId(),
                hotel.getHotelType(),
                hotel.getAddress(),
                hotel.getAddressDetail(),
                hotel.getRoomCnt(),
                hotel.getBedCnt(),
                hotel.getMaxPeople(),
                hotel.getFacility(),
                hotel.getNickname(),
                hotel.getDescription(),
                hotel.getPrice(),
                ImagesResponse.of(hotel.getImages()),
                hotel.getUnavailableDates(),
                hotel.getCreatedAt()
        );
    }
}
