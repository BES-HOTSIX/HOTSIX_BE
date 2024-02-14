package com.example.hotsix_be.review.controller;
<<<<<<< HEAD

=======
import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
>>>>>>> 2baeb0c1c7c28de853ccd48d7251a090ad8b28f8
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.dto.response.ReviewListWithSummaryResponse;
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

    @MemberOnly
    @PostMapping("/add/{hotelId}/{reservationId}")
    public ResponseEntity<?> addReview(@RequestBody final ReviewRequestDTO reviewRequestDTO,
                                       @PathVariable final Long hotelId,
                                       @PathVariable final Long reservationId,
                                       @Auth final Accessor accessor) {
        reviewService.addReview(reviewRequestDTO, hotelId, reservationId, accessor.getMemberId());
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
<<<<<<< HEAD
        List<ReviewResponseDTO> reviews = reviewService.getReviewsOrderByCreatedAtDesc(hotelId);
=======
        ReviewListWithSummaryResponse response = reviewService.getReviewsOrderByCreatedAtDesc(hotelId);
        List<ReviewResponseDTO> reviews = response.getReviews();
>>>>>>> 2baeb0c1c7c28de853ccd48d7251a090ad8b28f8
        return ResponseEntity.ok(reviews);
    }

    @MemberOnly
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable final Long id,
                                          @Auth final Accessor accessor) {
        reviewService.deleteReview(id, accessor.getMemberId());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.value(), "리뷰가 성공적으로 삭제되었습니다.", null, null, null));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewDetails(@PathVariable final Long id) {
        ReviewResponseDTO review = reviewService.getReviewDetails(id);
        return ResponseEntity.ok(review);
    }

    @MemberOnly
    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyReview(@PathVariable final Long id,
                                          @Valid @RequestBody final ReviewRequestDTO modifiedReviewDTO,
                                          @Auth final Accessor accessor
    ) {
        reviewService.modifyReview(id, modifiedReviewDTO, accessor.getMemberId());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "리뷰가 성공적으로 수정되었습니다.", null,
                        null, null
                )
        );
    }
<<<<<<< HEAD

}
    
=======
}
>>>>>>> 2baeb0c1c7c28de853ccd48d7251a090ad8b28f8
