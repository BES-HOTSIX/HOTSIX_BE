package com.example.hotsix_be.locations;

import org.locationtech.jts.geom.Point;

public interface LocationInfo {
    String getName();

    String getAddress();

    Point getLocation();
}
