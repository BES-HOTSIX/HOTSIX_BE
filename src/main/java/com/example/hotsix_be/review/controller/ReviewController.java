package com.example.hotsix_be.review.controller;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.exception.HotelException;
import com.example.hotsix_be.hotel.repository.HotelRepository;
import com.example.hotsix_be.member.service.MemberService;
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

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_HOTEL_ID;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final HotelRepository hotelRepository;

    @PostMapping("/add/{hotelId}")
    public ResponseEntity<?> addReview(@Valid @RequestBody final ReviewRequestDTO reviewRequestDTO,
                                       @PathVariable final Long hotelId) {

        log.info("Received review request: {}", reviewRequestDTO);
        reviewService.addReview(reviewRequestDTO, hotelId);
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

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO modifiedReviewDTO
    ) {
        log.info("Received modify review request for reviewId: {}", id);

        Long hotelId = modifiedReviewDTO.getHotelId();

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelException(NOT_FOUND_HOTEL_ID));

        reviewService.modifyReview(id, modifiedReviewDTO, hotel);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "리뷰가 성공적으로 수정되었습니다.", null,
                        null, null
                )
        );
    }

    }
    
