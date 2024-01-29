package com.example.hotsix_be.login.dto.naver;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class Response {
    private String id;
    private String nickname;
    private String profile_image;
}
