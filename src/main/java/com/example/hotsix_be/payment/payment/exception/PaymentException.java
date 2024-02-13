package com.example.hotsix_be.payment.payment.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class PaymentException extends BadRequestException {
    public PaymentException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
