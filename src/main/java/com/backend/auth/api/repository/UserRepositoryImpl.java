package com.backend.auth.api.repository;

import com.backend.auth.api.entity.User;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.backend.auth.api.entity.QOAuthAccount.*;
import static com.backend.auth.api.entity.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<User> getUserByProviderAndProviderUserId(OAuthProvider provider, String providerUserId) {
        User resultUser = queryFactory
                .selectFrom(user)
                .join(user.oauthAccounts, oAuthAccount)
                .where(
                        oAuthAccount.provider.eq(provider)
                        .and(oAuthAccount.providerUserId.eq(providerUserId))
                )
                .fetchOne();
        return Optional.ofNullable(resultUser);
    }


}