package com.backend.ljy.auth.api.repository;

import com.backend.ljy.auth.api.entity.OAuthAccount;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long>, OAuthAccountRepositoryCustom {
    Optional<OAuthAccount> findByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);
}