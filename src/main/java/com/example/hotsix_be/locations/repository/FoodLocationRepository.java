package com.example.hotsix_be.locations.repository;

import com.example.hotsix_be.locations.entity.FoodLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodLocationRepository extends JpaRepository<FoodLocation, Long> {
    @Query(value = "SELECT * FROM food_location " +
            "WHERE ST_CONTAINS(ST_BUFFER(ST_GeomFromText(CONCAT('POINT(', :lat, ' ', :lon, ')'), 4326), :length), location)", nativeQuery = true)
    List<FoodLocation> findNearbyLocations(@Param("lat") Double latitude, @Param("lon") Double longitude, @Param("length") Double distance);
}
