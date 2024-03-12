package com.example.hotsix_be.chat.dto.response;

import com.example.hotsix_be.chat.entity.ChatRoom;
import com.example.hotsix_be.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberChatRoomResponse {
	private final Long chatRoomId;
	private final String contactNickname;
	private final String contactImage;
	private final LocalDateTime latestDate;

	public static MemberChatRoomResponse of(final ChatRoom chatRoom, final Member contact, final LocalDateTime latestDate) {
		return new MemberChatRoomResponse(
				chatRoom.getId(),
				contact.getNickname(),
				contact.getImageUrl(),
				latestDate
		);
	}
}
