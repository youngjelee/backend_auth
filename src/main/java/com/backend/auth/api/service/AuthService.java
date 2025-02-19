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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieService cookieService;
    private final RedisService redisService;

    public ResponseEntity<?> getProfileByAccessToken(HttpServletRequest request) {

        String accessToken = cookieService.getAccessTokenParseServletRequest(request);

        if (accessToken == null) throw new AuthException(ResponseCode.UNAUTHORIZED);

        if (!jwtTokenProvider.validateToken(accessToken)) throw new AuthException(ResponseCode.AUTH_TOKEN_EXPIRED);

        // 토큰에서 사용자 정보 추출
        String userid = jwtTokenProvider.getUserid(accessToken);
        Claims claims = jwtTokenProvider.getClaims(accessToken);
        String nickname = claims.get("nickname", String.class);

        // email, profileImage는 토큰에 포함되지 않았다면 기본값으로 처리 (필요하면 DB 조회 등을 통해 채울 수 있음)
        UserProfileDto dto = new UserProfileDto();
        dto.setUserid(userid);
        dto.setNickname(nickname);

        // 추가적인 요소는 db 실시간 조회를 통해 직접 체크예정
        // dto.setEmail("");          // 토큰에 없으므로 빈 문자열 또는 null
        // dto.setProfileImage("");   // 토큰에 없으므로 빈 문자열 또는 null
        return ResponseUtils.success(dto);
    }



    public ResponseEntity<?> logout(HttpServletRequest request , HttpServletResponse response) {

        String accessToken = cookieService.getAccessTokenParseServletRequest(request);

        if( accessToken != null && jwtTokenProvider.validateToken(accessToken) ) {
            String userid = jwtTokenProvider.getUserid(accessToken);
            redisService.deleteRefreshToken(userid);
        }

        Cookie cookie = cookieService.makeAccessTokenExpire();
        response.addCookie(cookie);

        //redis 리프레시 토큰 삭제


        return ResponseUtils.success();
    }


}
