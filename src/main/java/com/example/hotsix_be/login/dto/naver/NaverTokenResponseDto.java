package com.example.hotsix_be.login.dto.naver;

import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class NaverTokenResponseDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
}
