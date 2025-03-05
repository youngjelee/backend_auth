package com.backend.ljy.auth.config.security.oauth.service;

import com.backend.ljy.auth.config.security.oauth.OAuthAttributes;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface OAuthAttributesConverter {
    OAuthAttributes convert( OAuth2AuthenticationToken token );
}
