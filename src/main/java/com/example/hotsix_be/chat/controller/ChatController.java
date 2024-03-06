package com.example.hotsix_be.chat.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.chat.dto.request.ChatMessageRequest;
import com.example.hotsix_be.chat.dto.request.ChatRoomCreateRequest;
import com.example.hotsix_be.chat.dto.response.ChatMessageResponse;
import com.example.hotsix_be.chat.dto.response.ChatRoomCreateResponse;
import com.example.hotsix_be.chat.dto.response.ChatRoomInfoResponse;
import com.example.hotsix_be.chat.service.ChatService;
import com.example.hotsix_be.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

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
		ChatRoomCreateResponse chatRoomCreateResponse = chatService.saveChatRoom(chatRoomCreateRequest, accessor.getMemberId());

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅방이 생성되었습니다.", null,
						null, chatRoomCreateResponse
				)
		);
	}

	@GetMapping("/api/v1/chat/info/{roomId}")
	@MemberOnly
	public ResponseEntity<?> getChatRoom(
			@PathVariable(value = "roomId") final Long roomId,
			@Auth final Accessor accessor
	) {
		ChatRoomInfoResponse chatRoomInfoResponse = chatService.getChatRoom(roomId, accessor.getMemberId());

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅방 정보를 불러왔습니다.", null,
						null, chatRoomInfoResponse
				)
		);
	}

	@MessageMapping("/chat.sendMessage/{roomId}")
	public void sendMessage(final @DestinationVariable Long roomId, final ChatMessageRequest chatMessageRequest) {
		ChatMessageResponse chatMessageResponse = chatService.saveMessage(roomId, chatMessageRequest);
		messagingTemplate.convertAndSend(String.format("/topic/messages/%s", roomId), chatMessageResponse);
	}
}
