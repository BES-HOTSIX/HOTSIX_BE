package com.example.hotsix_be.review.service;

import com.example.hotsix_be.reservation.repository.ReservationRepository;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.dto.response.ReviewResponseDTO;
import com.example.hotsix_be.review.entity.Review;
import com.example.hotsix_be.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void addReview(ReviewRequestDTO reviewRequestDTO) {
        Review review = new Review(
                reviewRequestDTO.getBody(),
                reviewRequestDTO.getAmenities(),
                reviewRequestDTO.getStaffService(),
                reviewRequestDTO.getCleanliness(),
                reviewRequestDTO.getRating(),
                reviewRequestDTO.getHotel(),
                reviewRequestDTO.getMember(),
                reviewRequestDTO.getReservation()
        );

        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        // Review 엔터티를 ReviewResponseDTO로 변환하여 반환
        return convertToReviewResponseDTOs(reviews);
    }

    private List<ReviewResponseDTO> convertToReviewResponseDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(this::convertToReviewResponseDTO)
                .collect(Collectors.toList());
    }

    private ReviewResponseDTO convertToReviewResponseDTO(Review review) {
        return new ReviewResponseDTO(
                review.getBody(),
                review.getAmenities(),
                review.getStaffService(),
                review.getCleanliness(),
                review.getRating()
        );
    }
}