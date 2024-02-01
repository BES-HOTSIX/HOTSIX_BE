package com.example.hotsix_be.review.service;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.dto.response.ReviewResponseDTO;
import com.example.hotsix_be.review.entity.Review;
import com.example.hotsix_be.review.exception.ReviewException;
import com.example.hotsix_be.review.repository.ReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_HOTEL_ID;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_REVIEW_ID;


@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;

    @Transactional
    public void addReview(final ReviewRequestDTO reviewRequestDTO, final Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        Review review = new Review(
                reviewRequestDTO.getBody(),
                reviewRequestDTO.getAmenities(),
                reviewRequestDTO.getStaffService(),
                reviewRequestDTO.getCleanliness(),
                reviewRequestDTO.getRating(),
                hotel
        );
        reviewRepository.save(review);
    }

    public List<ReviewResponseDTO> getReviewsOrderByCreatedAtDesc(final Long hotelId) {
        return reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(hotelId);
    }

    @Transactional
    public void modifyReview(final Long id, final @Valid ReviewRequestDTO reviewUpdateRequest, Hotel hotel) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));

        review.update(reviewUpdateRequest, hotel);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.findById(id).ifPresent(reviewRepository::delete);
    }


}