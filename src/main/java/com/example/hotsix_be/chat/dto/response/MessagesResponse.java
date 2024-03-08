package com.example.hotsix_be.chat.dto.response;

import com.example.hotsix_be.chat.entity.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MessagesResponse {
	private final List<MessageResponse> messageList;

	public static MessagesResponse of(final List<Message> messageList) {
		List<MessageResponse> messageResponseList = messageList.stream()
				.map(message -> new MessageResponse(
						message.getSender().getNickname(),
						message.getMessageText(),
						message.getCreatedAt()
				)).collect(Collectors.toList());

		return new MessagesResponse(messageResponseList);
	}
}
