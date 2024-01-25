package com.example.hotsix_be.login.service;


import com.example.hotsix_be.login.domain.RefreshToken;
import com.example.hotsix_be.login.repository.RefreshTokenRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate,
                               RefreshTokenRepository refreshTokenRepository) {
        this.redisTemplate = redisTemplate;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean isValidRefreshToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("refreshToken:" + token));
    }

}