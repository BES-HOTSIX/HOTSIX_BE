package com.example.hotsix_be.login.dto.kakao;

import lombok.Data;

@Data
public class KakaoTokenResponseDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
    private Integer refresh_token_expires_in;
}