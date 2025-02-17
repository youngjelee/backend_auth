package com.backend.auth.config.security.oauth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
// OAuth 를 통해 받은 값들을 auth 서버에서 쓰기좋도록 통합 및 인스턴스로 반환
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey; // id 식별값
    private String name;
    private String email;
    private String picture;
    // OAuth 제공자로부터 받은 고유 사용자 ID (예: 구글은 "sub")
    private String providerUserId;
    private String provider;




}