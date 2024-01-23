package com.example.hotsix_be.hotel.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class HotelInfoRequest {
    private String hotelType;
    private String hotelAddress;
    private String hotelDetailAddress;
    private Long numberOfBedrooms;
    private Long numberOfBeds;
    private Long numberOfBathrooms;
    private Long maximumGuests;
    private List<String> hotelAmenities;
    private String hotelName;
    private String hotelDescription;
    private Long hotelPricePerNight;
}
