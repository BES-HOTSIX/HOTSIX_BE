package com.example.hotsix_be.member.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.EmptyResponse;
import com.example.hotsix_be.member.dto.request.MemberPasswordChangeRequest;
import com.example.hotsix_be.member.dto.request.MemberRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @Operation(
            summary = "비밀번호 변경",
            description = "비밀번호 변경을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "비밀번호 변경 성공"
    )
    @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PutMapping("/password")
    @MemberOnly
    public ResponseEntity<?> changePassword(
            @RequestBody MemberPasswordChangeRequest memberPasswordChangeRequest,
            @Auth @Parameter(hidden = true) final Accessor accessor
    );

    @Operation(
            summary = "회원 예약 조회",
            description = "회원 예약 조회을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 예약 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "회원 예약 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/me/reservations")
    @MemberOnly
    public ResponseEntity<?> getMyReservations(
            @Parameter(hidden = true) @Auth final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @Operation(
            summary = "회원이 등록한 숙소 조회",
            description = "회원이 등록한 숙소의 조회를 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 호텔 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "회원 호텔 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/me/hotels")
    @MemberOnly
    public ResponseEntity<?> getMyHotels(
            @Auth @Parameter(hidden = true) final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @Operation(
            summary = "회원이 찜한 숙소 조회",
            description = "회원이 찜한 숙소의 조회를 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원이 찜한 숙소 조회 성공"
    )
    @ApiResponse(responseCode = "400", description = "회원이 찜한 숙소 조회 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/me/likes")
    @MemberOnly
    public ResponseEntity<?> getMyLikes(
            @Auth @Parameter(hidden = true) final Accessor accessor,
            @RequestParam(value = "page", defaultValue = "0") int page
    );

    @Operation(
            summary = "닉네임 중복 체크",
            description = "닉네임 중복 체크를 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "닉네임 중복 체크 성공"
    )
    @ApiResponse(responseCode = "400", description = "닉네임 중복 체크 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @GetMapping("/nickname/exists")
    public ResponseEntity<?> isExistNickname(@RequestParam("nickname") String nickname);

    @Operation(
            summary = "닉네임 변경",
            description = "닉네임 변경을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "닉네임 변경 성공"
    )
    @ApiResponse(responseCode = "400", description = "닉네임 변경 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PutMapping("/nickname")
    @MemberOnly
    public ResponseEntity<?> changeNickName(
            @Auth @Parameter(hidden = true) final Accessor accessor,
            @RequestParam("nickname") String nickname
    );

    @Operation(
            summary = "프로필 이미지 변경",
            description = "프로필 이미지 변경을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "프로필 이미지 변경 성공"
    )
    @ApiResponse(responseCode = "400", description = "프로필 이미지 변경 실패",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @ApiResponse(responseCode = "500", description = "서버 에러",
            content = @Content(schema = @Schema(implementation = EmptyResponse.class)))
    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @MemberOnly
    public ResponseEntity<?> changeImageUrl(
            @Auth @Parameter(hidden = true) final Accessor accessor,
            @RequestPart(value = "files", required = false) final List<MultipartFile> multipartFiles
    );

}
