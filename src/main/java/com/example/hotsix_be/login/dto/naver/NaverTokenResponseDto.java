package com.example.hotsix_be.login.dto.naver;

import lombok.Data;

@Data
public class NaverTokenResponseDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
}
