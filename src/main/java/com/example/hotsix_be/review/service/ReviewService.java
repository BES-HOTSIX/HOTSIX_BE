package com.example.hotsix_be.review.service;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
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

import java.util.DoubleSummaryStatistics;
import java.util.List;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;
import com.example.hotsix_be.review.dto.response.ReviewListWithSummaryResponse;
import com.example.hotsix_be.review.dto.response.ReviewSummaryResponse;

@RequiredArgsConstructor
@Service
@Transactional(readOnly=true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewResponseDTO addReview(final ReviewRequestDTO reviewRequestDTO, final Long hotelId, final Long reservationId, final Long memberId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationException(NOT_FOUND_RESERVATION_ID));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        Review review = new Review(
                reviewRequestDTO.getBody(),
                reviewRequestDTO.getAmenities(),
                reviewRequestDTO.getStaffService(),
                reviewRequestDTO.getCleanliness(),
                reviewRequestDTO.getRating(),
                hotel,
                reservation,
                member
        );
        reviewRepository.save(review);
        List<ReviewResponseDTO> reviews = reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(hotelId);

        ReviewListWithSummaryResponse response = calculateAndUpdateAverages(hotelId, reviews);

        return ReviewResponseDTO.of(review, response.getSummary());
    }

    public ReviewListWithSummaryResponse getReviewsOrderByCreatedAtDesc(final Long hotelId) {
        List<ReviewResponseDTO> reviews = reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(hotelId);
        ReviewListWithSummaryResponse response = calculateAndUpdateAverages(hotelId, reviews);
        return response;
    }

    @Transactional
    public void modifyReview(final Long id, final @Valid ReviewRequestDTO reviewUpdateRequest, final Long memberId) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        review.update(reviewUpdateRequest);
        reviewRepository.save(review);

        calculateAndUpdateAverages(review.getHotel().getId(), reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(review.getHotel().getId()));
    }

    @Transactional
    public void deleteReview(final Long id, final Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));
        Review reviewToDelete = reviewRepository.findById(id).orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));

        reviewRepository.delete(reviewToDelete);

        calculateAndUpdateAverages(reviewToDelete.getHotel().getId(), reviewRepository.findAllByHotelIdOrderByCreatedAtDesc(reviewToDelete.getHotel().getId()));
    }

    public ReviewResponseDTO getReviewDetails(final Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));
        ReviewSummaryResponse summary = calculateSummaryForReview(review);
        return ReviewResponseDTO.of(review, summary);
    }

    private ReviewSummaryResponse calculateSummaryForReview(Review review) {
        Double amenities = review.getAmenities();
        Double cleanliness = review.getCleanliness();
        Double staffService = review.getStaffService();
        Double rating = review.getRating();

        double totalAmenities = amenities;
        double totalCleanliness = cleanliness;
        double totalStaffService = staffService;
        double totalRating = rating;

        return new ReviewSummaryResponse(totalAmenities, totalCleanliness, totalStaffService, totalRating);
    }

    private ReviewListWithSummaryResponse calculateAndUpdateAverages(Long hotelId, List<ReviewResponseDTO> reviews) {
        DoubleSummaryStatistics amenitiesStats = reviews.stream().mapToDouble(ReviewResponseDTO::getAmenities).summaryStatistics();
        DoubleSummaryStatistics cleanlinessStats = reviews.stream().mapToDouble(ReviewResponseDTO::getCleanliness).summaryStatistics();
        DoubleSummaryStatistics staffServiceStats = reviews.stream().mapToDouble(ReviewResponseDTO::getStaffService).summaryStatistics();
        DoubleSummaryStatistics ratingStats = reviews.stream().mapToDouble(ReviewResponseDTO::getRating).summaryStatistics();

        double totalAmenities = amenitiesStats.getAverage();
        double totalCleanliness = cleanlinessStats.getAverage();
        double totalStaffService = staffServiceStats.getAverage();
        double totalRating = ratingStats.getAverage();

        for (ReviewResponseDTO review : reviews) {
            Review existingReview = reviewRepository.findById(review.getId()).orElseThrow(() -> new ReviewException(NOT_FOUND_REVIEW_ID));
            existingReview.updateAverages(totalAmenities, totalCleanliness, totalStaffService, totalRating);
            reviewRepository.save(existingReview);
        }

        ReviewSummaryResponse summary = new ReviewSummaryResponse(totalAmenities, totalCleanliness, totalStaffService, totalRating);
        return new ReviewListWithSummaryResponse(reviews, summary);
    }
}