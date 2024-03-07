package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findAllByChatRoomId(Long id);
}
