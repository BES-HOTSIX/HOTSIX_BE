package com.example.hotsix_be.like.controller;


import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.like.dto.request.LikeRequest;
import com.example.hotsix_be.like.dto.LikeStatus;
import com.example.hotsix_be.like.dto.response.LikeStatusResponse;
import com.example.hotsix_be.like.openapi.LikeApi;
import com.example.hotsix_be.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@RestController
public class LikeController implements LikeApi {

    private final LikeService likeService;

    @GetMapping("/status")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> getLikeStatus(@Auth final Accessor accessor,
                                                            @RequestParam final Long hotelId) {
        try {
            LikeStatus likeStatus = likeService.getLikeStatus(accessor.getMemberId(), hotelId);
            return ResponseEntity.ok(LikeStatusResponse.of(likeStatus.isLiked(), likeStatus.getLikesCount()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 좋아요 상태 토글
    @PostMapping("/toggle")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> toggleLike(@Auth final Accessor accessor,
                                                         @RequestBody final LikeRequest likeRequest) {
        try {
            LikeStatus likeStatus = likeService.toggleLike(accessor.getMemberId(), likeRequest.getHotelId());
            return ResponseEntity.ok(LikeStatusResponse.of(likeStatus.isLiked(), likeStatus.getLikesCount()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
