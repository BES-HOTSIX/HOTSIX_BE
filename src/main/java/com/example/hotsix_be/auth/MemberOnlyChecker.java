package com.example.hotsix_be.auth;

import com.example.hotsix_be.auth.util.Accessor;
import com.example.hotsix_be.common.exception.AuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.example.hotsix_be.common.exception.ExceptionCode.INVALID_AUTHORITY;

@Aspect
@Component
public class MemberOnlyChecker {

    @Before("@annotation(com.example.hotsix_be.auth.MemberOnly)")
    public void check(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(Accessor.class::isInstance)
                .map(Accessor.class::cast)
                .filter(Accessor::isMember)
                .findFirst()
                .orElseThrow(() -> new AuthException(INVALID_AUTHORITY));
    }
}
