package com.example.hotsix_be.chat.controller;

import com.example.hotsix_be.auth.Auth;
import com.example.hotsix_be.auth.MemberOnly;
import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.chat.dto.request.ChatRoomCreateRequest;
import com.example.hotsix_be.chat.dto.request.MessageSenderRequest;
import com.example.hotsix_be.chat.dto.response.ChatRoomCreateResponse;
import com.example.hotsix_be.chat.dto.response.ChatRoomInfoResponse;
import com.example.hotsix_be.chat.dto.response.MessagesResponse;
import com.example.hotsix_be.chat.service.ChatService;
import com.example.hotsix_be.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
	private final ChatService chatService;

	@PostMapping("")
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

	@GetMapping("/info/{roomId}")
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

	@GetMapping("/messages/{roomId}")
	@MemberOnly
	public ResponseEntity<?> getChatMessages(
			@PathVariable(value = "roomId") final Long roomId,
			@Auth final Accessor accessor
	) {
		MessagesResponse messagesResponse = chatService.getMessages(roomId, accessor.getMemberId());

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅 내역을 불러왔습니다.", null,
						null, messagesResponse
				)
		);
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> exitChatRoom(
			@PathVariable(value = "roomId") final Long roomId
	) {
		chatService.exitChatRoom(roomId);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅방에서 퇴장했습니다.", null,
						null, null
				)
		);
	}

	@PostMapping("/read/{roomId}")
	public ResponseEntity<?> readChatMessages(
			@PathVariable(value = "roomId") final Long roomId,
			@RequestBody MessageSenderRequest messageSenderRequest
	) {
		chatService.readChatMessages(roomId, messageSenderRequest);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"채팅방 메세지를 모두 읽었습니다.", null,
						null, null
				)
		);
	}
}
