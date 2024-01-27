package com.example.hotsix_be.login.service;


import static com.example.hotsix_be.common.exception.ExceptionCode.FAIL_TO_GENERATE_RANDOM_NICKNAME;
import static com.example.hotsix_be.common.exception.ExceptionCode.FAIL_TO_VALIDATE_TOKEN;
import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_REFRESH_TOKEN;
import static com.example.hotsix_be.common.exception.ExceptionCode.PASSWORD_NOT_MATCHED;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.login.domain.MemberTokens;
import com.example.hotsix_be.login.domain.OauthProviders;
import com.example.hotsix_be.login.domain.RefreshToken;
import com.example.hotsix_be.login.dto.kakao.KakaoPropertiesDto;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.response.LoginResponse;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import com.example.hotsix_be.login.util.BearerAuthorizationExtractor;
import com.example.hotsix_be.login.util.JwtProvider;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.channels.FileChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private static final int MAX_TRY_COUNT = 5;
    private static final int FOUR_DIGIT_RANGE = 10000;

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final OauthProviders oauthProviders;
    private final JwtProvider jwtProvider;
    private final BearerAuthorizationExtractor bearerExtractor;
    private final PasswordEncoder passwordEncoder;
    private final KakaoOauthService kakaoOAuthService;

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
                    KakaoPropertiesDto properties = userInfo.getProperties();

                    final Member member = kakaoOAuthService.registerMember(properties);
                    final MemberTokens memberTokens = jwtProvider.generateLoginToken(member.getId().toString());
                    final RefreshToken savedRefreshToken = new RefreshToken(memberTokens.getRefreshToken(), member.getId());
                    refreshTokenRepository.save(savedRefreshToken);

                    return LoginResponse.of(memberTokens.getRefreshToken(), memberTokens.getAccessToken());
                });
    }


    private Member findOrCreateMember(final String socialLoginId, final String nickname, final String imageUrl) {
        return memberRepository.findBySocialLoginId(socialLoginId)
                .orElseGet(() -> createMember(socialLoginId, nickname, imageUrl));
    }

    private Member createMember(final String socialLoginId, final String nickname, final String imageUrl) {
        int tryCount = 0;
        while (tryCount < MAX_TRY_COUNT) {
            final String nicknameWithRandomNumber = nickname + generateRandomFourDigitCode();
            if (!memberRepository.existsByNickname(nicknameWithRandomNumber)) {
                return null;
            }
            tryCount += 1;
        }
        throw new AuthException(FAIL_TO_GENERATE_RANDOM_NICKNAME);
    }

    private String generateRandomFourDigitCode() {
        final int randomNumber = (int) (Math.random() * FOUR_DIGIT_RANGE);
        return String.format("%04d", randomNumber);
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

    public void removeRefreshTokenProd(final String refreshToken, HttpServletResponse response) {

        refreshTokenRepository.deleteById(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .domain(".hotshare.me")
                .maxAge(0) // 쿠키의 유효기간을 0으로 설정하여 쿠키를 삭제
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());
    }

    public void removeRefreshTokenDev(final String refreshToken, HttpServletResponse response) {

        refreshTokenRepository.deleteById(refreshToken);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh-token", refreshToken)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(0) // 쿠키의 유효기간을 0으로 설정하여 쿠키를 삭제
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());
    }


    public void deleteAccount(final Long memberId) {
        memberRepository.deleteMemberById(memberId);
    }


}
