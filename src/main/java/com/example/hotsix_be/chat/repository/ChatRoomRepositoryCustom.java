package com.example.hotsix_be.chat.repository;

import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
	Page<ChatRoom> findAvailableChatRoomsByHostOrUserWithLatestMessage(Pageable pageable, Member member);
	Page<ChatRoom> findExitedChatRoomsByHostWithLatestMessage(Pageable pageable, Member member);
}
