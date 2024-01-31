package com.example.hotsix_be.review.repository;

import com.example.hotsix_be.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    List<Review> findAllByOrderByCreatedAtDesc();

    Optional<Review> findByUsername(String username);
}
