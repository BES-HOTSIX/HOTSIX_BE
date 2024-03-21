package com.example.hotsix_be.locations.service;

import com.example.hotsix_be.locations.entity.FoodLocation;
import com.example.hotsix_be.locations.repository.FoodLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final FoodLocationRepository foodLocationRepository;

    public List<FoodLocation> findNearbyLocations(Double latitude, Double longitude, Double distance) {
        return foodLocationRepository.findNearbyLocations(latitude, longitude, distance);
    }
}
