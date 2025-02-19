package com.backend.auth.api.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import static com.backend.auth.config.common.constants.CommonVariables.ACCESS_TOKEN_VALIDITY_SEC;

@Service
public class CookieService {

    private final String ACCESS_TOKEN = "access_token";


    /**
     * accessToken 기바능로 쿠키생성
     * */
    public Cookie makeAccessTokenCookie(String accessToken) {

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN, accessToken);
        accessTokenCookie.setHttpOnly(true);   // 클라이언트 자바스크립트에서 접근 못하도록 설정
        accessTokenCookie.setSecure(true);       // HTTPS 환경이라면 true 설정 (개발환경에서는 false일 수 있음)
        accessTokenCookie.setPath("/");

        accessTokenCookie.setMaxAge(ACCESS_TOKEN_VALIDITY_SEC - 10);
        return accessTokenCookie;
    }

    public Cookie makeAccessTokenExpire() {
        // access_token 쿠키 삭제: 값은 null, Max-Age를 0으로 설정하여 즉시 만료
        Cookie cookie = new Cookie(ACCESS_TOKEN, null);
        cookie.setHttpOnly(true);
        // 필요에 따라 쿠키의 Secure 속성도 설정 (https 환경)
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 0초로 설정하면 즉시 만료됩니다.
        return cookie;
    }


    /**
     * 서블릿 리퀘스트를 기반으로 access_tok
     */
    public String getAccessTokenParseServletRequest(HttpServletRequest request) {
        // 쿠키에서 "access_token" 값을 찾기
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }

        return accessToken;
    }


}
