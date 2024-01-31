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
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        log.info("Received review request: {}", reviewRequestDTO);
        reviewService.addReview(reviewRequestDTO);
        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "리뷰가 성공적으로 등록되었습니다.", null,
                        null, null
                )
        );
    }

    @GetMapping("/all")
    @ResponseBody
        public ResponseEntity<List<ReviewResponseDTO>> getAllReviewsOrderByCreatedAtDesc() {
            List<ReviewResponseDTO> reviews = reviewService.getAllReviewsOrderByCreatedAtDesc();
            return ResponseEntity.ok(reviews);
        }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "리뷰가 성공적으로 삭제되었습니다.", null, null, null));
    }
    }
    
