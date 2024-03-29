package com.example.hotsix_be.login;


import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.exception.RefreshTokenException;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String REFRESH_TOKEN = "refresh-token";

    private final JwtProvider jwtProvider;

    private final BearerAuthorizationExtractor extractor;

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Accessor resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new BadRequestException(INVALID_REQUEST);
        }

        try {
            final String refreshToken = extractRefreshToken(request.getCookies());
            final String accessToken = extractor.extractAccessToken(webRequest.getHeader(AUTHORIZATION));
            jwtProvider.validateTokens(new MemberTokens(refreshToken, accessToken));

            final Long memberId = Long.valueOf(jwtProvider.getSubject(accessToken));
            return Accessor.member(memberId);
        } catch (final RefreshTokenException e) {
            return Accessor.guest();
        }
    }

    private String extractRefreshToken(final Cookie... cookies) {
        if (cookies == null) {
            throw new RefreshTokenException(NOT_FOUND_REFRESH_TOKEN);
        }

        return Arrays.stream(cookies)
                .filter(this::isValidRefreshToken)
                .findFirst()
                .orElseThrow(() -> new RefreshTokenException(NOT_FOUND_REFRESH_TOKEN))
                .getValue();
    }

    private boolean isValidRefreshToken(final Cookie cookie) {
        return REFRESH_TOKEN.equals(cookie.getName()) &&
                refreshTokenRepository.existsById(cookie.getValue());
    }
}
