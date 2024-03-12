package com.example.hotsix_be.locations.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FoodLocation extends Location {
}
