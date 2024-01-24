package com.example.hotsix_be.hotel.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class HotelException extends BadRequestException {
    public HotelException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
