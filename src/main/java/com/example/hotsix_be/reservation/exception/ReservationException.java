package com.example.hotsix_be.reservation.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class ReservationException extends BadRequestException {
	public ReservationException(final ExceptionCode exceptionCode) {
			super(exceptionCode);
		}
}
