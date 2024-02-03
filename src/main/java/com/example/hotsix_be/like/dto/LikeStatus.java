package com.example.hotsix_be.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "좋아요 상태")
@Getter
public class LikeStatus {

    @Schema(description = "좋아요 체크 여부", example = "true")
    private final boolean liked;

    @Schema(description = "좋아요 개수", example = "3")
    private final int likesCount;

    public LikeStatus(final boolean liked, final int likesCount) {
        this.liked = liked;
        this.likesCount = likesCount;
    }
}
