package com.backend.ljy.auth.api.repository;

import com.backend.ljy.auth.api.entity.OAuthAccount;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;

import java.util.Optional;

public interface OAuthAccountRepositoryCustom {
    /**
     * provider와 providerUserId로 OAuthAccount를 조회하며,  연관된 User까지 fetch join으로 한 번에 가져옴
     */
    Optional<OAuthAccount> getOAuthAccountByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}