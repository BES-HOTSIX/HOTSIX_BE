package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

	List<ChatRoom> findAllByHostIdAndUserId(Long hostId, Long userId);
}
