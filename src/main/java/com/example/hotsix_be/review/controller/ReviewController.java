package com.example.hotsix_be.review.controller;

import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.dto.response.ReviewResponseDTO;
import com.example.hotsix_be.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add/{hotelId}/{reservationId}")
    public ResponseEntity<?> addReview(@Valid @RequestBody final ReviewRequestDTO reviewRequestDTO,
                                       @PathVariable final Long hotelId,
                                       @PathVariable final Long reservationId) {

        log.info("Received review request: {}", reviewRequestDTO);
        reviewService.addReview(reviewRequestDTO, hotelId, reservationId);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "리뷰가 성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/{hotelId}")
    @ResponseBody
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsOrderByCreatedAtDesc(@PathVariable final Long hotelId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsOrderByCreatedAtDesc(hotelId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable final Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "리뷰가 성공적으로 삭제되었습니다.", null, null, null));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewDetails(@PathVariable final Long id) {
        ReviewResponseDTO review = reviewService.getReviewDetails(id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO modifiedReviewDTO
    ) {
        log.info("Received modify review request for reviewId: {}", id);

//        Long hotelId = modifiedReviewDTO.getHotelId();
//
//        Hotel hotel = hotelRepository.findById(hotelId)
//                .orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        reviewService.modifyReview(id, modifiedReviewDTO);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "리뷰가 성공적으로 수정되었습니다.", null,
                        null, null
                )
        );
    }

}
    
