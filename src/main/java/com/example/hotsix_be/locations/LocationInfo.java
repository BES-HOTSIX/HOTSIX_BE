package com.example.hotsix_be.locations;

import org.locationtech.jts.geom.Point;

public interface LocationInfo {
    Long getId();

    String getName();

    String getAddress();

    Point getLocation();
}
