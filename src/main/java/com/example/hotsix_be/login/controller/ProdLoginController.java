package com.example.hotsix_be.login.controller;

import static com.example.hotsix_be.common.exception.ExceptionCode.NOT_SUPPORTED_OAUTH_SERVICE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.request.OAuthCodeRequest;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile("prod")
public class ProdLoginController {

    public static final int COOKIE_AGE_SECONDS = 604800;

    private final LoginService loginService;
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> loginProd(@RequestBody LoginRequest loginRequest, final HttpServletResponse response) {

        Member member = memberService.getMemberByUsername(loginRequest.getUsername());

        LoginResponse loginResponse = loginService.login(loginRequest, member);

        sendRefreshTokenCookieProd(response, loginResponse);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "성공적으로 로그인 되었습니다.", null,
                        null, loginResponse.getAccessToken()
                )
        );
    }

    @PostMapping("/login/{provider}")
    public Mono<ResponseEntity<ResponseDto<String>>> OAuthLoginDev(
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
            sendRefreshTokenCookieProd(httpServletResponse, loginResponse);
            return Mono.just(ResponseEntity.ok(
                    new ResponseDto<>(
                            HttpStatus.OK.value(),
                            "성공적으로 로그인 되었습니다.", null, null, loginResponse.getAccessToken()
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

    @PostMapping("/token")
    public ResponseEntity<?> extendLogin(
            @CookieValue("refresh-token") final String refreshToken,
            @RequestHeader("Authorization") final String authorizationHeader
    ) {
        final String renewalAccessToken = loginService.renewalAccessToken(refreshToken, authorizationHeader);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "엑세스 토큰이 재발급 되었습니다.", null,
                        null, renewalAccessToken
                )
        );
    }

    @DeleteMapping("/user/logout")
    @MemberOnly
    public ResponseEntity<Void> logoutProd(
            @Auth final Accessor accessor,
            @CookieValue("refresh-token") final String refreshToken, HttpServletResponse response) {
        loginService.removeRefreshTokenProd(refreshToken, response);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account")
    @MemberOnly
    public ResponseEntity<Void> deleteAccount(@Auth final Accessor accessor) {
        loginService.deleteAccount(accessor.getMemberId());
        return ResponseEntity.noContent().build();
    }

    private void sendRefreshTokenCookieProd(final HttpServletResponse response,
                                           final LoginResponse loginResponse) {
        final ResponseCookie cookie = ResponseCookie.from("refresh-token", loginResponse.getRefreshToken())
                .maxAge(COOKIE_AGE_SECONDS)
                .domain(".hotshare.me")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        response.addHeader(SET_COOKIE, cookie.toString());
    }

}

