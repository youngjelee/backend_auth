package com.backend.auth.config.security.oauth.service.impl;

import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class KakaoOAuthAttributesConverter implements OAuthAttributesConverter {


    @Override
    public OAuthAttributes convert(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
        String name = (profile != null) ? (String) profile.get("nickname") : null;
        String picture = (profile != null) ? (String) profile.get("profile_image_url") : null;
        String email = (kakaoAccount != null) ? (String) kakaoAccount.get("email") : null;
        String providerUserId = String.valueOf(attributes.get("id"));

        return OAuthAttributes.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .providerUserId(providerUserId)
                .provider(userRequest.getClientRegistration().getRegistrationId())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}