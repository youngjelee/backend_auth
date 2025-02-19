package com.backend.auth.config.security.oauth.service.impl;

import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

public class KakaoOAuthAttributesConverter implements OAuthAttributesConverter {

 @Override
    public OAuthAttributes convert(OAuth2AuthenticationToken token  ) {

        OAuth2User oAuth2User = token.getPrincipal();
        Map<String, Object> attribute = oAuth2User.getAttributes();

        Map<String,Object> property = oAuth2User.getAttribute("properties");
        Map<String,Object> kakaoAccount = (HashMap<String,Object>)(HashMap<String,Object>)oAuth2User.getAttribute("kakao_account");

        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuthAttributes.builder()
                .name((String) property.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .providerUserId( String.valueOf(attribute.get("id"))
                )
                .provider(OAuthProvider.getByRegistrationId(token.getAuthorizedClientRegistrationId()))
                .build();
    }

}