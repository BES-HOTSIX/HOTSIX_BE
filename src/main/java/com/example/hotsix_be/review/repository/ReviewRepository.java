package com.example.hotsix_be.review.repository;

import com.example.hotsix_be.review.dto.response.ReviewResponseDTO;
import com.example.hotsix_be.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<ReviewResponseDTO> findAllByHotelIdOrderByCreatedAtDesc(Long hotelId);

    Optional<Review> findById(Long id);

    Optional<Review> findByReservationId(Long id);

    boolean existsByMemberIdAndHotelId(Long memberId, Long hotelId);
}