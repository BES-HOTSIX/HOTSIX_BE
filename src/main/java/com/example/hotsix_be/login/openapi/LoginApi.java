package com.example.hotsix_be.login.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.like.dto.response.LikeStatusResponse;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.request.OAuthCodeRequest;
import com.example.hotsix_be.login.dto.response.AccessTokenResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

@Tag(name = "Login", description = "로그인 관련 API")
public interface LoginApi {

    @Operation(
            summary = "로그인",
            description = "로그인을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공"
    )
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<AccessTokenResponse>> login(@RequestBody final LoginRequest loginRequest,
                                                                  final HttpServletResponse response);


    @Operation(
            summary = "소셜 로그인",
            description = "소셜 로그인을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "소셜 로그인 성공"
    )
    @PostMapping("/login/{provider}")
    public Mono<ResponseEntity<ResponseDto<AccessTokenResponse>>> OAuthLogin(
            @PathVariable final String provider,
            @RequestBody OAuthCodeRequest oAuthCodeRequest,
            final HttpServletResponse response
    );


    @Operation(
            summary = "엑세스 토큰 갱신",
            description = "엑세스 토큰 갱신을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "엑세스 토큰 갱신 성공"
    )
    @PostMapping("/token")
    public ResponseEntity<ResponseDto<String>> extendLogin(
            @CookieValue("refresh-token") @Parameter(hidden = true) final String refreshToken,
            @RequestHeader("Authorization")  final String authorizationHeader
    );


    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공"
    )
    @DeleteMapping("/user/logout")
    @MemberOnly
    public ResponseEntity<Void> logout(
            @Auth @Parameter(hidden = true) final Accessor accessor,
            @CookieValue("refresh-token") @Parameter(hidden = true) final String refreshToken, HttpServletResponse response);


    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 탈퇴 성공"
    )
    @DeleteMapping("/account")
    @MemberOnly
    public ResponseEntity<Void> deleteAccount(@Auth @Parameter(hidden = true) final Accessor accessor);
}
