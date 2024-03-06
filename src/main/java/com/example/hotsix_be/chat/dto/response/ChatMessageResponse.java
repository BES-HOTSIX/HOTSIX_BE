package com.example.hotsix_be.chat.dto.response;

import com.example.hotsix_be.chat.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ChatMessageResponse {
	private final String content;
	private final String sender;
	private final LocalDateTime timestamp;

	public static ChatMessageResponse of(final Message message) {
		return new ChatMessageResponse(
				message.getMessageText(),
				message.getSender().getNickname(),
				message.getCreatedAt()
		);
	}
}
