package com.example.hotsix_be.location.service;


import com.example.hotsix_be.locations.entity.FoodLocation;
import com.example.hotsix_be.locations.repository.FoodLocationRepository;
import com.example.hotsix_be.locations.service.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
    @Mock
    private FoodLocationRepository foodLocationRepository;

    @InjectMocks
    private LocationService locationService;

    @DisplayName("주변 음식점을 찾을 수 있다.")
    @Test
    void findNearbyLocations() {
        // given
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location1 = geometryFactory.createPoint(new Coordinate(37.5665, 126.9780));
        Point location2 = geometryFactory.createPoint(new Coordinate(37.5770, 126.9760));

        FoodLocation foodLocation1 = FoodLocation.builder()
                .name("식당1")
                .address("주소1")
                .location(location1)
                .build();

        FoodLocation foodLocation2 = FoodLocation.builder()
                .name("식당2")
                .address("주소2")
                .location(location2)
                .build();

        List<FoodLocation> foodLocations = Arrays.asList(foodLocation1, foodLocation2);

        given(foodLocationRepository.findNearbyLocations(any(), any(), any())).willReturn(foodLocations);

        // when
        List<FoodLocation> nearbyLocations = locationService.findNearbyLocations(37.5665, 126.9780, 1.0);

        // then
        assertThat(nearbyLocations).isNotNull();
    }
}
