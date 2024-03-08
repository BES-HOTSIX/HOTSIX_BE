package com.example.hotsix_be.chat.dto.response;

import com.example.hotsix_be.chat.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ChatRoomInfoResponse {
	private final String hostNickname;
	private final String userNickname;

	public static ChatRoomInfoResponse of(final ChatRoom chatRoom) {
		return new ChatRoomInfoResponse(
				chatRoom.getHost().getNickname(),
				chatRoom.getUser().getNickname()
		);
	}
}
