package com.example.hotsix_be.chat.controller;

import com.example.hotsix_be.chat.dto.request.ChatMessageRequest;
import com.example.hotsix_be.chat.dto.response.ChatMessageResponse;
import com.example.hotsix_be.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatService chatService;

	@MessageMapping("/chat.sendMessage/{roomId}")
	public void sendMessage(final @DestinationVariable Long roomId, final ChatMessageRequest chatMessageRequest) {
		ChatMessageResponse chatMessageResponse = chatService.saveMessage(roomId, chatMessageRequest);
		messagingTemplate.convertAndSend(String.format("/topic/messages/%s", roomId), chatMessageResponse);
	}
}
