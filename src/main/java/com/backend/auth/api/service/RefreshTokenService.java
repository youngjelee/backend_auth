package com.backend.auth.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    // Refresh Token 유효기간 (예: 24시간 = 86400초)
    private final long refreshTokenValiditySeconds = 86400L;

    public void saveRefreshToken(String key, String refreshToken) {
        // key는 보통 사용자 ID나 username을 사용 (예: "refreshToken:{username}")
        redisTemplate.opsForValue().set("refreshToken:" + key, refreshToken, refreshTokenValiditySeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get("refreshToken:" + key);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete("refreshToken:" + key);
    }
}