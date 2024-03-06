package com.example.hotsix_be.chat.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class ChatException extends BadRequestException {
	public ChatException(final ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
