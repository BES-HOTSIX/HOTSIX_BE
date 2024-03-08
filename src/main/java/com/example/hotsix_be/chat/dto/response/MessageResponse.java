package com.example.hotsix_be.chat.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@RequiredArgsConstructor(access = PROTECTED)
public class MessageResponse {
	private final String sender;	// nickname
	private final String content;
	private final LocalDateTime timestamp;
}
