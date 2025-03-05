package com.backend.ljy.auth.config.security.handler;

import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.api.service.CookieService;
import com.backend.ljy.auth.api.service.RedisService;
import com.backend.ljy._config.common.base.ApiResponse;
import com.backend.ljy._config.exception.ResponseCode;
import com.backend.ljy.auth.config.security.jwt.JwtTokenProvider;
import com.backend.ljy.auth.config.security.oauth.OAuthAttributes;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.ljy.auth.config.security.service.CustomUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler_V2 extends SimpleUrlAuthenticationSuccessHandler {

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

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuthAttributes attributes = OAuthProvider.getByRegistrationId(token.getAuthorizedClientRegistrationId())
                .createAttributes(token);

        User user = customUserService.findOrCreateUserByOAuthAttributes(attributes);

        // 토큰 생성
        String accessToken = jwtTokenProvider.createToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        redisService.saveRefreshToken(user.getUserid(), refreshToken);

        // JSON 형태로 토큰 정보를 응답 본문에 작성
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 응답 데이터 구성
        var tokenMap = new java.util.HashMap<String, String>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        ApiResponse<HashMap<String, String>> successLoginInfo = ApiResponse.of(ResponseCode.SUCCESS, tokenMap);


        String jsonString = new ObjectMapper().writeValueAsString(successLoginInfo);

        // HTML 응답 생성 (팝업에서 postMessage 수행)
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 팝업 창에서 메인 창으로 메시지를 보내고, 팝업을 닫는 스크립트
        String scriptHtml = "<html><body>"
                + "<script>"
                + "   var responseData = " + jsonString + ";"
                + "   window.opener.postMessage(responseData, '*');"
                + "   window.close();"
                + "</script>"
                + "</body></html>";

        response.getWriter().write(scriptHtml);
        response.getWriter().flush();
    }
}
