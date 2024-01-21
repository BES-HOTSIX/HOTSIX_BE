package com.example.hotsix_be.hotel.dto;

import java.util.List;
import lombok.Data;

@Data
public class HotelInfoDto {
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
