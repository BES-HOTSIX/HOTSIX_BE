package com.example.hotsix_be.like.dto.response;


import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class LikeStatusResponse {
    private final boolean liked;
    private final int likesCount;

    public static LikeStatusResponse of(final boolean liked, final int likesCount) {
        return new LikeStatusResponse(liked, likesCount);
    }

}