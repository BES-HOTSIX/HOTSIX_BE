package com.example.hotsix_be.member.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.member.dto.request.MemberPasswordChangeRequest;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "회원 관련 API")
public interface MemberApi {

    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 위한 API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "회원 가입 성공"
    )
    @ApiResponse(responseCode = "400", description = "회원 가입 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRegisterRequest memberRegisterRequest);


    @Operation(
            summary = "회원 정보 조회",
            description = "회원 정보 조회을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "회원 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/info")
    @MemberOnly
    public ResponseEntity<?> getMemberInfo(@Auth @Parameter(hidden = true) final Accessor accessor);

    @PutMapping("/password")
    @MemberOnly
    public ResponseEntity<?> changePassword(
            @RequestBody MemberPasswordChangeRequest memberPasswordChangeRequest,
            @Auth final Accessor accessor
    );

    @GetMapping("/me/reservations")
    @MemberOnly
    @Operation(security = {
            @SecurityRequirement(name = "Authorization"),
            @SecurityRequirement(name = "refreshToken")
    })
    public ResponseEntity<?> getMyReservations(
            @Parameter(hidden = true) @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @GetMapping("/me/hotels")
    @MemberOnly
    public ResponseEntity<?> getMyHotels(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @GetMapping("/me/likes")
    @MemberOnly
    public ResponseEntity<?> getMyLikes(
            @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @GetMapping("/nickname/exists")
    public ResponseEntity<?> isExistNickname(@RequestParam("nickname") String nickname);

    @PutMapping("/nickname")
    @MemberOnly
    public ResponseEntity<?> changeNickName(
            @Auth final Accessor accessor,
            @RequestParam("nickname") String nickname
    );

    @PutMapping("/image")
    @MemberOnly
    public ResponseEntity<?> changeImageUrl(
            @Auth final Accessor accessor,
            @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles
    );

}
