package com.example.hotsix_be.chat.controller;

import com.example.hotsix_be.chat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
	private final SimpMessagingTemplate messagingTemplate;

//	@PostMapping("/api/v1/chat/create")
//	@MemberOnly
//	public ResponseEntity<?> createChatRoom(
//			@RequestBody final ReservationInfoRequest reservationInfoRequest,
//			@Auth final Accessor accessor
//	) {
//		ReservationCreateResponse reservationCreateResponse = reservationService.save(hotelId, reservationInfoRequest, accessor.getMemberId());
//
//		return ResponseEntity.ok(
//				new ResponseDto<>(
//						HttpStatus.OK.value(),
//						"예약 내역이 생성되었습니다.", null,
//						null, null
//				)
//		);
//	}

	@MessageMapping("/chat.sendMessage")
	public void sendMessage(Message message) {
		messagingTemplate.convertAndSend("/topic/messages", message);
	}
}
