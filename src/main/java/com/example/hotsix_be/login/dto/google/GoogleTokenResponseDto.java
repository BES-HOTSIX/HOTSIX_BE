package com.example.hotsix_be.login.dto.google;

import lombok.Data;

@Data
public class GoogleTokenResponseDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
    private String id_token; // Google 응답에만 있는 필드
}