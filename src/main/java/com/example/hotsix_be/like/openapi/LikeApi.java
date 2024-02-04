package com.example.hotsix_be.like.openapi;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.dto.ResponseDto;
import com.example.hotsix_be.like.dto.LikeStatus;
import com.example.hotsix_be.like.dto.request.LikeRequest;
import com.example.hotsix_be.like.dto.response.LikeStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Like", description = "찜 관련 API")
public interface LikeApi {

    @Operation(
            summary = "찜 상태 조회",
            description = "찜 상태 조회를 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "찜 조회 성공",
            content = @Content(
                    schema = @Schema(implementation = LikeStatusResponse.class)
            )
    )
    @Parameter(
            name = "hotelId",
            description = "찜 상태를 조회할 호텔 id",
            required = true
    )
    @GetMapping("/status")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> getLikeStatus(@Auth @Parameter(hidden = true) final Accessor accessor,
                                                            @RequestParam final Long hotelId);

    // 좋아요 상태 토글

    @Operation(
            summary = "찜 상태 토글",
            description = "찜 상태 토글을 위한 API"
    )
    @ApiResponse(
            responseCode = "200",
            description = "찜 상태 토글 성공",
            content = @Content(
                    schema = @Schema(implementation = LikeStatusResponse.class)
            )
    )
    @PostMapping("/toggle")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> toggleLike(@Auth @Parameter(hidden = true) final Accessor accessor,
                                                         @RequestBody final LikeRequest likeRequest);

}
