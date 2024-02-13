package com.example.hotsix_be.hotel.repository;

import com.example.hotsix_be.hotel.entity.Hotel;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelRepositoryCustom {

    Page<Hotel> findAllByDistrictAndDate(Pageable pageable, String district, LocalDate startDate,
                                         LocalDate endDate, String kw, Long bedroomCount, Long bedCount,
                                         Long bathroomCount, Long maxGuestCount, Long price);

    Page<Hotel> findByLikesCountAndCreatedAt(Pageable pageable);

    Page<Hotel> findByReservationsCountAndCreatedAt(Pageable pageable);
}
