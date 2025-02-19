package com.backend.auth.api.repository;

import com.backend.auth.api.entity.User;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> getUserByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);

}
