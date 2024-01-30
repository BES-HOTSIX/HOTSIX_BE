package com.example.hotsix_be.like.repository;


import com.example.hotsix_be.like.entity.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndHotelId(Long memberId, Long hotelId);

    int countByHotelIdAndLiked(Long hotelId, boolean b);

}

