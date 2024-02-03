package com.example.hotsix_be.login.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.like.dto.response.LikeStatusResponse;
import com.example.hotsix_be.login.dto.request.LoginRequest;
import com.example.hotsix_be.login.dto.request.OAuthCodeRequest;
import com.example.hotsix_be.login.dto.response.AccessTokenResponse;
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
            description = "로그인 성공",
            content = @Content(
            schema = @Schema(implementation = ResponseDto.class)
    )
    )
    @Parameter(
            name = "loginRequest",
            description = "로그인 할 회원 정보",
            required = false
    )
    @Parameter(
            name = "response",
            description = "HttpServletResponse 객체",
            required = false
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest,
                                   final HttpServletResponse response);


    @Operation(
            summary = "소셜 로그인",
            description = "소셜 로그인을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "소셜 로그인 성공"
    )
    @Parameter(
            name = "provider",
            description = "소셜 로그인 제공 업체",
            required = false
    )
    @Parameter(
            name = "oAuthCodeRequest",
            description = "소셜 로그인 요청을 위한 code",
            required = false
    )
    @Parameter(
            name = "response",
            description = "HttpServletResponse 객체",
            required = false
    )
    @PostMapping("/login/{provider}")
    public Mono<ResponseEntity<ResponseDto<AccessTokenResponse>>> OAuthLogin(
            @PathVariable String provider,
            @RequestBody OAuthCodeRequest oAuthCodeRequest,
            final HttpServletResponse response
    );


    @Operation(
            summary = "엑세스 토큰 갱신",
            description = "엑세스 토큰 갱신을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "엑세스 토큰 갱신 성공",
            content = @Content(
            schema = @Schema(implementation = ResponseDto.class)
    )
    )
    @Parameter(
            name = "refresh-token",
            description = "리프레시 토큰",
            required = false
    )
    @Parameter(
            name = "Authorization",
            description = "Authorization 헤더",
            required = false
    )
    @PostMapping("/token")
    public ResponseEntity<?> extendLogin(
            @CookieValue("refresh-token") final String refreshToken,
            @RequestHeader("Authorization") final String authorizationHeader
    );


    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공",
            content = @Content(
            schema = @Schema(implementation = ResponseDto.class)
    )
    )
    @Parameter(
            name = "refresh-token",
            description = "리프레시 토큰",
            required = false
    )
    @Parameter(
            name = "response",
            description = "HttpServletResponse 객체",
            required = false
    )
    @DeleteMapping("/user/logout")
    @MemberOnly
    public ResponseEntity<Void> logout(
            @Auth final Accessor accessor,
            @CookieValue("refresh-token") final String refreshToken, HttpServletResponse response);


    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴을 위한 API"
    )
    @Parameter(
            name = "accessor",
            description = "회원 id를 가진 Accessor 객체",
            required = false
    )
    @DeleteMapping("/account")
    @MemberOnly
    public ResponseEntity<Void> deleteAccount(@Auth final Accessor accessor);
}
