package com.example.hotsix_be.locations.dto.Response;

import com.example.hotsix_be.locations.LocationInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationResponse<T extends LocationInfo> {
    private final String name;
    private final String address;
    private final Coordinate coord;

    public static <T extends LocationInfo> LocationResponse<T> of(final T location) {
        return new LocationResponse<T>(
                location.getName(),
                location.getAddress(),
                location.getLocation().getCoordinate()
        );
    }
}
