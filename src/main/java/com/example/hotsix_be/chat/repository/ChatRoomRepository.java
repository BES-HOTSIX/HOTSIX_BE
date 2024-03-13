package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

	Optional<ChatRoom> findByHostId(Long id);

	Page<ChatRoom> findByHostOrUserOrderByIdDesc(Pageable pageable, Member member1, Member member2);
}
