package com.example.hotsix_be.like.controller;



import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.like.dto.request.LikeRequestDto;
import com.example.hotsix_be.like.dto.LikeStatus;
import com.example.hotsix_be.like.dto.response.LikeStatusResponse;
import com.example.hotsix_be.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@RestController
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/status")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> getLikeStatus(@Auth Accessor accessor, @RequestParam Long hotelId) {
        try {
            LikeStatus likeStatus = likeService.getLikeStatus(accessor.getMemberId(), hotelId);
            return ResponseEntity.ok(new LikeStatusResponse(likeStatus.isLiked(), likeStatus.getLikesCount()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 좋아요 상태 토글
    @PostMapping("/toggle")
    @MemberOnly
    public ResponseEntity<LikeStatusResponse> toggleLike(@Auth Accessor accessor, @RequestBody LikeRequestDto likeRequestDto) {
        try {
            LikeStatus likeStatus = likeService.toggleLike(accessor.getMemberId(), likeRequestDto.getHotelId());
            return ResponseEntity.ok(new LikeStatusResponse(likeStatus.isLiked(), likeStatus.getLikesCount()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
