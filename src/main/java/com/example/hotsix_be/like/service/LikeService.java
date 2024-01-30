package com.example.hotsix_be.like.service;


import static com.example.hotsix_be.common.exception.ExceptionCode.*;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.hotel.repository.HotelRepository;

import com.example.hotsix_be.like.dto.LikeStatus;
import com.example.hotsix_be.like.entity.Like;
import com.example.hotsix_be.like.repository.LikeRepository;
import com.example.hotsix_be.member.entity.Member;
import com.example.hotsix_be.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    private final HotelRepository hotelRepository;

    private final MemberRepository memberRepository;

    // 좋아요 상태 조회
    public LikeStatus getLikeStatus(Long memberId, Long hotelId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->  new AuthException(NOT_FOUND_HOTEL_ID));

        Like like = likeRepository.findByMemberIdAndHotelId(memberId, hotelId)
                .orElse(new Like(member, hotel, false)); // 좋아요가 없는 경우

        int likesCount = likeRepository.countByHotelIdAndLiked(hotelId, true);
        return new LikeStatus(like.isLiked(), likesCount);
    }

    // 좋아요 상태 토글
    @Transactional
    public LikeStatus toggleLike(Long memberId, Long hotelId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(NOT_FOUND_MEMBER_BY_ID));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->  new AuthException(NOT_FOUND_HOTEL_ID));

        Optional<Like> likeOpt = likeRepository.findByMemberIdAndHotelId(memberId, hotelId);
        Like like;

        if (likeOpt.isPresent()) {
            // 이미 좋아요가 존재하는 경우, 상태 토글
            like = likeOpt.get();
            like.toggleLiked();

            if (!like.isLiked()) {
                hotel.decrementLikesCount();
            } else {
                hotel.incrementLikesCount();
            }
        } else {
            // 좋아요가 존재하지 않는 경우, 새로 생성
            like = new Like(member, hotel, true);
            hotel.incrementLikesCount();
        }

        likeRepository.save(like);
        hotelRepository.save(hotel);

        return new LikeStatus(like.isLiked(), hotel.getLikesCount());
    }

}
