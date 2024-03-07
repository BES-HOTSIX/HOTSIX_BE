package com.example.hotsix_be.chat.dto.response;

import com.example.hotsix_be.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ChatRoomCreateResponse {
	private final Long chatRoomId;

	public static ChatRoomCreateResponse of(final ChatRoom chat) {
		return new ChatRoomCreateResponse(
				chat.getId()
		);
	}
}
