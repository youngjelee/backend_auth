package com.backend.auth.config.security.oauth.provider.impl;

import com.backend.auth.config.security.oauth.provider.OAuth2UserInfo;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Naver의 경우 응답 JSON 내부의 "response" 객체에 사용자 정보가 담깁니다.
     * "id" 필드를 사용자 고유 식별자로 사용합니다.
     */
    @Override
    public String getProviderId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            return (String) response.get("id");
        }
        return null;
    }

    /**
     * Naver 응답에서 사용자 이름은 "name" 필드에 있으며,
     * 만약 없으면 "nickname"도 사용할 수 있습니다.
     */
    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            String name = (String) response.get("name");
            if (name == null) {
                name = (String) response.get("nickname");
            }
            return name;
        }
        return null;
    }

    /**
     * Naver 응답에서 사용자 이메일은 "email" 필드에 있습니다.
     */
    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            return (String) response.get("email");
        }
        return null;
    }

    /**
     * Naver 응답에서 프로필 이미지 URL은 "profile_image" 필드에 있습니다.
     */
    @Override
    public String getImageUrl() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            return (String) response.get("profile_image");
        }
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}