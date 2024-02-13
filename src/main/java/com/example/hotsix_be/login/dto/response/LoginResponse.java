package com.example.hotsix_be.login.dto.response;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class LoginResponse {

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSIsImV4cCI6MTYyNzIwNzIwMH0.")
    private String refreshToken;

    @Schema(description = "엑세스 토큰", example = "eyJhbGci")
    private String accessToken;

    public static LoginResponse of(String refreshToken, String accessToken) {
        return new LoginResponse(refreshToken, accessToken);
    }
}
