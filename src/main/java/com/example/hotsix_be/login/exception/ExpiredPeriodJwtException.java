package com.example.hotsix_be.login.exception;

import com.example.hotsix_be.common.exception.AuthException;
import com.example.hotsix_be.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ExpiredPeriodJwtException extends AuthException {

    public ExpiredPeriodJwtException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
