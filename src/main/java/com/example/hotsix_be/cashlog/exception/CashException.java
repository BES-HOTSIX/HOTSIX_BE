package com.example.hotsix_be.cashlog.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class CashException extends BadRequestException {
    public CashException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
