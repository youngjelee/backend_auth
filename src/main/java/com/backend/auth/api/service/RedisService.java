package com.backend.auth.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.backend.auth.config.common.constants.CommonVariables.REFRESH_TOKEN_VALIDITY_SEC;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;


    public void saveRefreshToken(String key, String refreshToken) {
        log.info("create refreshToken:{} ",key );
        redisTemplate.opsForValue().set("refreshToken:" + key, refreshToken, REFRESH_TOKEN_VALIDITY_SEC, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get("refreshToken:" + key);
    }

    public void deleteRefreshToken(String key) {
        log.info("delete key ============> refreshToken:{} ",key );
        redisTemplate.delete("refreshToken:" + key);
    }
}