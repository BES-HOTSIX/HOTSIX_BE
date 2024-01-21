package com.example.hotsix_be.hotel.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class HotelNotFoundException extends BadRequestException {
        public HotelNotFoundException(final ExceptionCode exceptionCode) {
            super(exceptionCode);
        }
}
