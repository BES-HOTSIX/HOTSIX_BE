package com.example.hotsix_be.review.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class ReviewException extends BadRequestException {
    public ReviewException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}