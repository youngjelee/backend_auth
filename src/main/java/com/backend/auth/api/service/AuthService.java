package com.backend.auth.api.service;

import com.backend.auth.api.dto.UserProfileDto;
import com.backend.auth.config.common.base.ApiResponse;
import com.backend.auth.config.common.util.ResponseUtils;
import com.backend.auth.config.exception.AuthException;
import com.backend.auth.config.exception.ResponseCode;
import com.backend.auth.config.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieService cookieService;
    private final RedisService redisService;

    public ResponseEntity<?> getProfileByAccessToken(HttpServletRequest request) {

        // Authorization 헤더에서 "Bearer {token}" 형식으로 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException(ResponseCode.UNAUTHORIZED);
        }
        String accessToken = authHeader.substring(7);

        if (!jwtTokenProvider.validateToken(accessToken))
            throw new AuthException(ResponseCode.AUTH_TOKEN_EXPIRED);

        // 토큰에서 사용자 정보 추출
        String userid = jwtTokenProvider.getUserid(accessToken);
        Claims claims = jwtTokenProvider.getClaims(accessToken);
        String nickname = claims.get("nickname", String.class);
        List<String> roles = claims.get("roles", List.class);

        // 토큰에 포함되지 않은 값은 기본값 처리 (필요 시 DB 조회 등)
        UserProfileDto dto = new UserProfileDto();
        dto.setUserid(userid);
        dto.setNickname(nickname);
        dto.setRoles(roles);

        return ResponseUtils.success(dto);
    }


    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        String accessToken = cookieService.getAccessTokenParseServletRequest(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            String userid = jwtTokenProvider.getUserid(accessToken);
            redisService.deleteRefreshToken(userid);
        }

        Cookie cookie = cookieService.makeAccessTokenExpire();
        response.addCookie(cookie);

        //redis 리프레시 토큰 삭제


        return ResponseUtils.success();
    }


}
