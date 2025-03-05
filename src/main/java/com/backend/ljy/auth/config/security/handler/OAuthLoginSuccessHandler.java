package com.backend.ljy.auth.config.security.handler;

import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.api.service.CookieService;
import com.backend.ljy.auth.api.service.RedisService;
import com.backend.ljy.auth.config.security.jwt.JwtTokenProvider;
import com.backend.ljy.auth.config.security.oauth.OAuthAttributes;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.ljy.auth.config.security.service.CustomUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final CustomUserService customUserService;
    private final CookieService cookieService;

    @Value("${front.url}")
    private String FRONT_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰

        OAuthAttributes attributes = OAuthProvider.getByRegistrationId(token.getAuthorizedClientRegistrationId())
                                     .createAttributes( token );

        User user = customUserService.findOrCreateUserByOAuthAttributes(attributes);


        // 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        redisService.saveRefreshToken(user.getUserid(), refreshToken);

        // 쿠키 생성
        Cookie accessTokenCookie = cookieService.makeAccessTokenCookie( accessToken);

        // 쿠키를 응답에 추가
        response.addCookie(accessTokenCookie);


        String redirectUri = FRONT_URL+"/oauth/callback/success";
        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
