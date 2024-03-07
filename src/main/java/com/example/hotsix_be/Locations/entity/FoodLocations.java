package com.example.hotsix_be.Locations.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
public class FoodLocations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Column(columnDefinition = "POINT SRID 4326")
    private Point location;
}
