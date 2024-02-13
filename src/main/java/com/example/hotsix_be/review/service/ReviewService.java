package com.example.hotsix_be.review.service;

import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.reservation.entity.Reservation;
import com.example.hotsix_be.reservation.exception.ReservationException;
import com.example.hotsix_be.reservation.repository.ReservationRepository;
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

import static com.example.hotsix_be.common.exception.ExceptionCode.*;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReviewResponseDTO addReview(final ReviewRequestDTO reviewRequestDTO, final Long hotelId, final Long reservationId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));

        Review review = new Review(
                reviewRequestDTO.getBody(),
                reviewRequestDTO.getAmenities(),
                reviewRequestDTO.getStaffService(),
                reviewRequestDTO.getCleanliness(),
                reviewRequestDTO.getRating(),
                hotel,
                reservation
        );
        reviewRepository.save(review);
        return ReviewResponseDTO.of(review);
    }

    public List<ReviewResponseDTO> getReviewsOrderByCreatedAtDesc(final Long hotelId) {
        return reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(hotelId);
    }

    @Transactional
    public void modifyReview(final Long id, final @Valid ReviewRequestDTO reviewUpdateRequest) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));

        review.update(reviewUpdateRequest);
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.findById(id).ifPresent(reviewRepository::delete);
    }


    public ReviewResponseDTO getReviewDetails(Long id) {
        return ReviewResponseDTO.of(
                reviewRepository.findById(id).orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID)));
    }
}