package com.example.hotsix_be.login.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberTokens {

    private final String refreshToken;
    private final String accessToken;
}
