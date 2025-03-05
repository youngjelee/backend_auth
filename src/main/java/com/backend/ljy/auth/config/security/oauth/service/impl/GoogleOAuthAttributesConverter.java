package com.backend.ljy.auth.config.security.oauth.service.impl;

import com.backend.ljy.auth.config.security.oauth.OAuthAttributes;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.ljy.auth.config.security.oauth.service.OAuthAttributesConverter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class GoogleOAuthAttributesConverter implements OAuthAttributesConverter {

    @Override
    public OAuthAttributes convert(OAuth2AuthenticationToken token  ) {

        OAuth2User oAuth2User = token.getPrincipal();
        Map<String,Object> attributes = oAuth2User.getAttributes();
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .providerUserId((String) attributes.get("sub"))
                .provider(OAuthProvider.getByRegistrationId(token.getAuthorizedClientRegistrationId()))
                .build();
    }


}