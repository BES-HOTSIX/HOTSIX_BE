package com.example.hotsix_be.login.dto.google;

import lombok.Data;

@Data
public class GoogleTokenResponseDto {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String scope;
}
