package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
	List<Message> findAllByChatRoomId(Long id);
	Optional<Message> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
	@Modifying
	@Query("UPDATE Message m SET m.isRead = true WHERE m.chatRoom.id = :chatRoomId AND m.sender.id != :memberId")
	int markMessagesAsReadByChatRoomId(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);

	@Query("SELECT COUNT(m) from Message m WHERE m.chatRoom.id = :chatRoomId AND m.isRead = false")
	int countByChatRoomIdAndIsReadFalse(@Param("chatRoomId") Long chatRoomId);
}
