package com.example.hotsix_be.image.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ImageException extends BadRequestException {

    public ImageException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
