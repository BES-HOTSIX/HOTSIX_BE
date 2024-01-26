package com.example.hotsix_be.login.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class LoginResponse {

    private String refreshToken;
    private String accessToken;
    private Long memberId;

   public static LoginResponse of(String refreshToken, String accessToken, Long memberId) {
        return new LoginResponse(refreshToken, accessToken, memberId);
    }
}
