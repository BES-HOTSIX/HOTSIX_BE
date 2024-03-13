package com.example.hotsix_be.coupon.exception;

import com.example.hotsix_be.common.exception.BadRequestException;
import com.example.hotsix_be.common.exception.ExceptionCode;

public class CouponException extends BadRequestException {
    public CouponException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
