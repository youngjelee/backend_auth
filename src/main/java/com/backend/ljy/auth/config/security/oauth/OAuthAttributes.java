package com.backend.ljy.auth.config.security.oauth;

import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

// OAuth 를 통해 받은 값들을 auth 서버에서 쓰기좋도록 통합 및 인스턴스로 반환
public class OAuthAttributes {

    private String providerUserId;  // 유일한 key 값
    private OAuthProvider provider;
    private String name;
    private String email;
    private String picture;




}