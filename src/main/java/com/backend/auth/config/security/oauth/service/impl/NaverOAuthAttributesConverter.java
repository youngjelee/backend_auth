package com.backend.auth.config.security.oauth.service.impl;

import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

public class NaverOAuthAttributesConverter implements OAuthAttributesConverter {

    @Override
    public OAuthAttributes convert(OAuth2AuthenticationToken token) {

        OAuth2User oAuth2User = token.getPrincipal();
        Map<String,Object> attribute = oAuth2User.getAttribute("response");

        return OAuthAttributes.builder()
                .name((String) attribute.get("name"))
                .email((String) attribute.get("email"))
                .providerUserId((String) attribute.get("id"))
                .provider(OAuthProvider.getByRegistrationId(token.getAuthorizedClientRegistrationId()))
                .build();
    }

}