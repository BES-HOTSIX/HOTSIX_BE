package com.example.hotsix_be.touristSpot.controller;

import com.example.hotsix_be.touristSpot.service.TouristSpotService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RestController
public class TouristSpotController {

    @Autowired
    private TouristSpotService touristSpotService;

    @GetMapping("/v1/search/local.json")
    public Object searchLocal(@RequestParam String query) {
        // 주소 검색 서비스 호출
        return touristSpotService.searchTouristSpot(query);
    }
}