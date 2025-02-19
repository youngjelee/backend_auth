package com.backend.auth.config.security.oauth.enums;

import com.backend.auth.config.common.fi.TriFunction;
import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import com.backend.auth.config.security.oauth.service.impl.GoogleOAuthAttributesConverter;
import com.backend.auth.config.security.oauth.service.impl.KakaoOAuthAttributesConverter;
import com.backend.auth.config.security.oauth.service.impl.NaverOAuthAttributesConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * OAUTH 로그인을 지원하는 Enum
 * */

@Getter
public enum OAuthProvider {
    NAVER("naver", NaverOAuthAttributesConverter::new),
    KAKAO("kakao", KakaoOAuthAttributesConverter::new),
    GOOGLE("google", GoogleOAuthAttributesConverter::new),
    GITHUB("github", () -> {
        //TODO :: 구현 필요
        throw new UnsupportedOperationException("GITHUB converter not implemented yet");
    });

    @Getter
    private final String registrationId;
    private final Supplier<OAuthAttributesConverter> converterSupplier;
    private static final Map<String, OAuthProvider> lookup = new HashMap<>();

    static {
        for (OAuthProvider provider : OAuthProvider.values()) {
            lookup.put(provider.getRegistrationId().toLowerCase(), provider);
        }
    }

    OAuthProvider(String registrationId, Supplier<OAuthAttributesConverter> converterSupplier) {
        this.registrationId = registrationId;
        this.converterSupplier = converterSupplier;
    }

    public OAuthAttributes createAttributes(OAuth2AuthenticationToken token ) {
        return converterSupplier.get().convert(token);
    }

    public static OAuthProvider getByRegistrationId(String registrationId) {
        if (registrationId == null) {
            //TODO :: exception 추후
            throw new RuntimeException("미지원 중인 플랫폼입니다. ");
        }
        return lookup.get(registrationId.toLowerCase());
    }

}