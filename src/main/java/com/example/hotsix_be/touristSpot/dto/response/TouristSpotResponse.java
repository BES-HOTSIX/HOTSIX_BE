package com.example.hotsix_be.touristSpot.dto.response;

import lombok.*;

import static lombok.AccessLevel.PRIVATE;

@ToString
@NoArgsConstructor(access = PRIVATE)
@Getter

public class TouristSpotResponse {
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;

    public TouristSpotResponse(String title, String link, String category, String description, String telephone, String address, String roadAddress, String mapx, String mapy) {
    }

    public static TouristSpotResponse of(String title, String link, String category, String description,
                                         String telephone, String address, String roadAddress, String mapx, String mapy) {
        return new TouristSpotResponse(title, link, category, description, telephone, address, roadAddress, mapx, mapy);
    }

}