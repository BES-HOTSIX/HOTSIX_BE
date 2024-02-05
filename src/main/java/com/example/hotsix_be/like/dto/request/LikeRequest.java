package com.example.hotsix_be.like.dto.request;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "좋아요 요청")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class LikeRequest {

    @Schema(description = "숙소 ID", example = "1")
    private Long hotelId;
}
