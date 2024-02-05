package com.example.hotsix_be.login.service;


import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.domain.RefreshToken;
import com.example.hotsix_be.login.dto.kakao.KakaoProperties;
import com.example.hotsix_be.login.dto.naver.Response;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.response.LoginResponse;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    public static final int COOKIE_AGE_SECONDS = 604800;
    public static final String COOKIE_SAME_SITE = "Strict";

    @Value("${spring.cookie.domain}")
    private String cookieDomain;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    private final BearerAuthorizationExtractor bearerExtractor;

    private final PasswordEncoder passwordEncoder;

    private final KakaoOauthService kakaoOAuthService;

    private final GoogleOauthService googleOAuthService;

    private final NaverOauthService naverOAuthService;

    public LoginResponse login(final LoginRequest loginRequest, final Member member) {

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new AuthException(PASSWORD_NOT_MATCHED);
        }

        final MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
        final RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(), member.getId());
        refreshTokenRepository.save(savedRefreshToken);

        return LoginResponse.of(memberTokens.getRefreshToken(), memberTokens.getAccessToken());
    }

    public Mono<LoginResponse> KakaoOauthLogin(final String code) {
        return kakaoOAuthService.getToken(code)
                .flatMap(token -> {
                    String accessToken = token.getAccess_token();
                    log.info("accessToken : {}", accessToken);

                    return kakaoOAuthService.getMemberInfo(accessToken);
                })
                .map(userInfo -> {
                    KakaoProperties properties = userInfo.getProperties();

                    final Member member = kakaoOAuthService.registerMember(properties);
                    final MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
                    final RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(),
                            member.getId());
                    refreshTokenRepository.save(savedRefreshToken);

                    return LoginResponse.of(memberTokens.getRefreshToken(), memberTokens.getAccessToken());
                });
    }

    public Mono<LoginResponse> googleOauthLogin(final String code) {
        return googleOAuthService.getToken(code)
                .flatMap(token -> {
                    String accessToken = token.getAccess_token();
                    log.info("Google accessToken : {}", accessToken);

                    return googleOAuthService.getUserInfo(accessToken);
                })
                .map(userInfo -> {

                    final Member member = googleOAuthService.registerGoogleMember(userInfo);
                    final MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
                    final RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(),
                            member.getId());
                    refreshTokenRepository.save(savedRefreshToken);

                    return LoginResponse.of(memberTokens.getRefreshToken(), memberTokens.getAccessToken());
                });
    }

    public Mono<LoginResponse> naverOauthLogin(final String code, final String state) {
        return naverOAuthService.getToken(code, state)
                .flatMap(token -> {
                    String accessToken = token.getAccess_token();
                    log.info("naver accessToken : {}", accessToken);

                    return naverOAuthService.getMemberInfo(accessToken);
                })
                .map(userInfo -> {

                    Response properties = userInfo.getResponse();

                    final Member member = naverOAuthService.registerMember(properties);
                    final MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
                    final RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(),
                            member.getId());
                    refreshTokenRepository.save(savedRefreshToken);

                    return LoginResponse.of(memberTokens.getRefreshToken(), memberTokens.getAccessToken());
                });
    }


    public String renewalAccessToken(final String refreshTokenRequest, final String authorizationHeader) {
        final String accessToken = bearerExtractor.extractAccessToken(authorizationHeader);
        if (jwtProvider.isValidRefreshAndInvalidAccess(refreshTokenRequest, accessToken)) {
            final RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenRequest)
                    .orElseThrow(() -> new AuthException(INVALID_REFRESH_TOKEN));
            return jwtProvider.regenerateAccessToken(refreshToken.getMemberId().toString());
        }
        if (jwtProvider.isValidRefreshAndValidAccess(refreshTokenRequest, accessToken)) {
            return accessToken;
        }
        throw new AuthException(FAIL_TO_VALIDATE_TOKEN);
    }

    public void sendRefreshTokenCookie(final HttpServletResponse httpServletResponse,
                                       final LoginResponse loginResponse) {

        log.info("cookieDomain : {}", cookieDomain);
        ResponseCookie cookie = ResponseCookie.from("refresh-token", loginResponse.getRefreshToken())
                .maxAge(COOKIE_AGE_SECONDS)
                .secure(true)
                .httpOnly(true)
                .sameSite(COOKIE_SAME_SITE)
                .path("/")
                .domain(cookieDomain) // 환경별 도메인 사용
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
    }


    public void removeRefreshToken(final String refreshToken, final HttpServletResponse response) {

        refreshTokenRepository.deleteById(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(0) // 쿠키의 유효기간을 0으로 설정하여 쿠키를 삭제
                .domain(cookieDomain) // 환경별 도메인 사용
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());
    }

    public void deleteAccount(final Long memberId) {
        memberRepository.deleteMemberById(memberId);
    }


}
