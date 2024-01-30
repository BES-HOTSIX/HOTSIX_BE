package com.example.hotsix_be.like.dto.response;


import lombok.Getter;


@Getter
public class LikeStatusResponse {
    private final boolean liked;
    private final int likesCount;

    public LikeStatusResponse(boolean liked, int likesCount) {
        this.liked = liked;
        this.likesCount = likesCount;
    }

}