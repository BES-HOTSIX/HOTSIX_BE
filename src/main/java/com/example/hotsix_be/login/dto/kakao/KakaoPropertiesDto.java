package com.example.hotsix_be.login.dto.kakao;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class KakaoPropertiesDto {
    private String nickname;
    private String profile_image;
    private String thumbnail_image;
}
