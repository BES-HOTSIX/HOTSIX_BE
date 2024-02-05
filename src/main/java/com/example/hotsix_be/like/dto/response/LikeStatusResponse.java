package com.example.hotsix_be.like.dto.response;


import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Schema(description = "좋아요 상태 응답")
@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class LikeStatusResponse {

    @Schema(description = "좋아요 체크 여부", example = "true")
    private final boolean liked;

    @Schema(description = "좋아요 개수", example = "3")
    private final int likesCount;

    public static LikeStatusResponse of(final boolean liked, final int likesCount) {
        return new LikeStatusResponse(liked, likesCount);
    }

}