package com.example.hotsix_be.like.dto.request;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class LikeRequestDto {

    private Long hotelId;
}
