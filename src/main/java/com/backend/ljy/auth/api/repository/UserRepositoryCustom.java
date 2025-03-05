package com.backend.ljy.auth.api.repository;

import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> getUserByProviderAndProviderUserId(OAuthProvider provider, String providerUserId);

}
