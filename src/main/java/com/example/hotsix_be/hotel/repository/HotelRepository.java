package com.example.hotsix_be.hotel.repository;

import com.example.hotsix_be.hotel.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Page<Hotel> findAllByOrderByCreatedAtDesc(Pageable sortedPageable);

    Page<Hotel> findByOwnerIdOrderByIdDesc(Pageable pageable, Long memberId);
}
