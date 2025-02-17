package com.backend.auth.config.security.oauth.service.impl;

import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleOAuthAttributesConverter implements OAuthAttributesConverter {
    @Override
    public OAuthAttributes convert(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .providerUserId((String) attributes.get("sub"))
                .provider(userRequest.getClientRegistration().getRegistrationId())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}