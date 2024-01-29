package com.example.hotsix_be.login.controller;

import static com.example.hotsix_be.common.exception.ExceptionCode.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;


import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.login.dto.request.OAuthCodeRequest;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.response.AccessTokenResponse;
import com.example.hotsix_be.login.dto.response.LoginResponse;
import com.example.hotsix_be.login.service.LoginService;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile("dev")
public class DevLoginController {

    public static final int COOKIE_AGE_SECONDS = 604800;

    private final LoginService loginService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> loginDev(@RequestBody final LoginRequest loginRequest,
                                      final HttpServletResponse response) {

        Member member = memberService.getMemberByUsername(loginRequest.getUsername());

        LoginResponse loginResponse = loginService.login(loginRequest, member);

        sendRefreshTokenCookieDev(response, loginResponse);

     new AccessTokenResponse(loginResponse.getAccessToken());

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 로그인 되었습니다.", null,
                        null, new AccessTokenResponse(loginResponse.getAccessToken())
                )
        );
    }

    @PostMapping("/login/{provider}")
    public Mono<ResponseEntity<ResponseDto<AccessTokenResponse>>> OAuthLoginDev(
            @PathVariable String provider,
            @RequestBody OAuthCodeRequest oAuthCodeRequest,
            final HttpServletResponse httpServletResponse
    ) {
        String code = oAuthCodeRequest.getCode();
        String state = oAuthCodeRequest.getState();

        Mono<LoginResponse> loginResponseMono = switch (provider.toLowerCase()) {
            case "kakao" -> loginService.KakaoOauthLogin(code);
            case "google" -> loginService.googleOauthLogin(code);
            case "naver" -> loginService.naverOauthLogin(code, state);
            default -> Mono.error(new AuthException(NOT_SUPPORTED_OAUTH_SERVICE));
        };

        return loginResponseMono.flatMap(loginResponse -> {
            sendRefreshTokenCookieDev(httpServletResponse, loginResponse);
            return Mono.just(ResponseEntity.ok(
                    new ResponseDto<>(
                            HttpStatus.OK.value(),
                            "성공적으로 로그인 되었습니다.", null, null, new AccessTokenResponse(loginResponse.getAccessToken())
                    )
            ));
        }).onErrorResume(AuthException.class, e ->
                Mono.just(ResponseEntity.badRequest().body(
                        new ResponseDto<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "로그인에 실패하였습니다.", null, null, null
                        )
                ))
        );
    }

    @DeleteMapping("/user/logout")
    @MemberOnly
    public ResponseEntity<Void> logoutDev(
            @Auth final Accessor accessor,
            @CookieValue("refresh-token") final String refreshToken, HttpServletResponse response) {
        loginService.removeRefreshTokenDev(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    @MemberOnly
    public ResponseEntity<Void> deleteAccount(@Auth final Accessor accessor) {
        loginService.deleteAccount(accessor.getMemberId());
        return ResponseEntity.noContent().build();
    }


    private void sendRefreshTokenCookieDev(final HttpServletResponse httpServletResponse,
                                                  final LoginResponse loginResponse) {
        ResponseCookie cookie = ResponseCookie.from("refresh-token", loginResponse.getRefreshToken())
                .maxAge(COOKIE_AGE_SECONDS)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        httpServletResponse.addHeader(SET_COOKIE, cookie.toString());
    }

}
