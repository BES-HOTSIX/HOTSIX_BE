package com.example.hotsix_be.like.repository;


import com.example.hotsix_be.hotel.entity.Hotel;
import com.example.hotsix_be.like.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndHotelId(Long memberId, Long hotelId);

    int countByHotelIdAndLiked(Long hotelId, boolean b);

    @Query("SELECT l.hotel FROM Like l WHERE l.member.id = :memberId AND l.liked = true")
    Page<Hotel> findLikedHotelsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}

