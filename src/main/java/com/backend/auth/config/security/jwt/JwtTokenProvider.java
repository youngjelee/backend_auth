package com.backend.auth.config.security.jwt;


import com.backend.auth.api.entity.User;
import com.backend.auth.api.entity.enums.UserRole;
import com.backend.auth.config.common.constants.CommonVariables;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

import static com.backend.auth.config.common.constants.CommonVariables.REFRESH_TOKEN_VALIDITY_SEC;

@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;  // application.yml에서 설정한 값


    @PostConstruct
    protected void init() {
        // secretKey를 Base64 인코딩
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * 토큰 생성
     */
    public String createToken(String username, UserRole role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role.getValue());

        Date now = new Date();
        Date validity = new Date(now.getTime() + CommonVariables.ACCESS_TOKEN_VALIDITY_MIL);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }



    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 토큰에서 사용자 아이디 추출
     */
    public String getUserid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public String createToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + CommonVariables.ACCESS_TOKEN_VALIDITY_MIL);

        return Jwts.builder()
                   .setSubject(String.valueOf(user.getUserid()))
                   .claim("nickname", user.getNickname())
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                   .compact();
    }


    public String createRefreshToken(User user ) {

        Date now = new Date();
        // 리프레시 토큰 유효기간을 24시간으로 설정 (예시)
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_SEC);

        return Jwts.builder()
                   .setSubject(String.valueOf(user.getUserid()))
                   .claim("nickname", user.getNickname())
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                   .compact();
    }

    /**
     * 토큰의 Claims 추출 (추가된 메서드)
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

}