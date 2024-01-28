package com.example.hotsix_be.login.util;


import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_ACCESS_TOKEN;

import com.example.hotsix_be.login.exception.InvalidJwtException;
import org.springframework.stereotype.Component;

@Component
public class BearerAuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer ";

    public String extractAccessToken(String header) {
        if (header != null && header.startsWith(BEARER_TYPE)) {
            return header.substring(BEARER_TYPE.length()).trim();
        }
        throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
    }
}
