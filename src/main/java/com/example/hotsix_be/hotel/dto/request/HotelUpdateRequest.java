package com.example.hotsix_be.hotel.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelUpdateRequest {
    private String hotelType;
    private String address;
    private String addressDetail;
    private Long roomCnt;
    private Long bedCnt;
    private Long bathroomCnt;
    private Long maxPeople;
    private List<String> facility;
    private String nickname;
    private String description;
    private Long price;
}
