package com.backend.auth.config.security.oauth.provider.impl;


import com.backend.auth.config.security.oauth.provider.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * 구글의 경우 고유 식별자는 'sub' 필드에 있습니다.
     */
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    /**
     * 구글 응답에서는 'name' 필드가 사용자 이름입니다.
     */
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    /**
     * 구글 응답에서는 'email' 필드가 사용자 이메일입니다.
     */
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    /**
     * 구글 응답에서는 'picture' 필드가 프로필 이미지 URL입니다.
     */
    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

    /**
     * 원본 attributes 전체 반환
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}