package com.example.hotsix_be.login.dto.naver;

import lombok.Data;

@Data
public class NaverUserInfoDto {
    private String resultcode;
    private String message;
    private Response response;
}
