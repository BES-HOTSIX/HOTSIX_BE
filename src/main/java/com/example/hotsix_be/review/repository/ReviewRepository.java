package com.example.hotsix_be.review.repository;

import com.example.hotsix_be.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
