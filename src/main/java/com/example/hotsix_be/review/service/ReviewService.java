package com.example.hotsix_be.review.service;

import com.example.hotsix_be.reservation.repository.ReservationRepository;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.entity.Review;
import com.example.hotsix_be.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void addReview(ReviewRequestDTO reviewRequestDTO) {
        Review review = Review.builder()
                .body(reviewRequestDTO.getBody())
                .amenities(reviewRequestDTO.getAmenities())
                .staffService(reviewRequestDTO.getStaffService())
                .cleanliness(reviewRequestDTO.getCleanliness())
                .totalRating(calculateTotalRating(reviewRequestDTO))
                .reservation(reviewRequestDTO.getReservation())
                .build();

        reviewRepository.save(review);
    }

    // 리뷰 총점 계산
    private Double calculateTotalRating(ReviewRequestDTO reviewRequestDTO) {
        return (reviewRequestDTO.getAmenities() + reviewRequestDTO.getStaffService() + reviewRequestDTO.getCleanliness()) / 3;
    }
}