package com.example.hotsix_be.review;

import com.example.hotsix_be.review.dto.request.ReviewRequestDTO;
import com.example.hotsix_be.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class ReviewInit {

    private final ReviewService reviewService;

    @Autowired
    public ReviewInit(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Bean
    public ApplicationRunner initReview() {
        return args -> {
            for (int i = 0; i < 10; i++) {
                work1();
            }
        };
    }

    @Transactional
    public void work1() {
        ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(
                "The efficiency we have at removing trash has made creating trash more acceptable. Douglas figured the best way to succeed was to do the opposite of what he'd been doing all his life.",
                5.0,
                4.0,
                3.0,
                4.0,
                1L,
                1L,
                null
        );

        reviewService.addReview(reviewRequestDTO);
    }
}