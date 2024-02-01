package com.example.hotsix_be.like.dto;

import lombok.Getter;

@Getter
public class LikeStatus {
    private final boolean liked;
    private final int likesCount;

    public LikeStatus(final boolean liked, final int likesCount) {
        this.liked = liked;
        this.likesCount = likesCount;
    }
}
