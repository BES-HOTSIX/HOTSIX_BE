package com.example.hotsix_be.locations.controller;

import com.example.hotsix_be.locations.dto.Response.LocationResponse;
import com.example.hotsix_be.locations.entity.FoodLocation;
import com.example.hotsix_be.locations.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService foodLocationService;

    @GetMapping("/food")
    public List<LocationResponse<FoodLocation>> getFoodLocations(
            @RequestParam(value = "latitude") final Double latitude,
            @RequestParam(value = "longitude") final Double longitude,
            @RequestParam(value = "distance") final Double distance
    ) {
        List<FoodLocation> foodLocations = foodLocationService.findNearbyLocations(latitude, longitude, distance);

        return foodLocations.stream()
                .map(LocationResponse::of)
                .toList();
    }
}
