package com.example.hotsix_be.chat.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.chat.dto.request.ChatRoomCreateRequest;
import com.example.hotsix_be.chat.dto.response.ChatRoomCreateResponse;
import com.example.hotsix_be.chat.entity.Message;
import com.example.hotsix_be.chat.service.ChatService;
import com.example.hotsix_be.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService;

	@PostMapping("/api/v1/chat/create")
	@MemberOnly
	public ResponseEntity<?> createChatRoom(
			@RequestBody final ChatRoomCreateRequest chatRoomCreateRequest,
			@Auth final Accessor accessor
	) {
		ChatRoomCreateResponse chatRoomCreateResponse = chatService.save(chatRoomCreateRequest, accessor.getMemberId());

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅방이 생성되었습니다.", null,
						null, chatRoomCreateResponse
				)
		);
	}

	@MessageMapping("/chat.sendMessage/{roomId}")
	public void sendMessage(final @DestinationVariable String roomId, final Message message) {
		messagingTemplate.convertAndSend(String.format("/topic/messages/%s", roomId), message);
	}
}
