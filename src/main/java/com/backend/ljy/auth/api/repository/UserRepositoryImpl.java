package com.backend.ljy.auth.api.repository;

import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.config.security.oauth.enums.OAuthProvider;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.backend.ljy.api.entity.QOAuthAccount.*;
import static com.backend.ljy.api.entity.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<User> getUserByProviderAndProviderUserId(OAuthProvider provider, String providerUserId) {
        User resultUser = queryFactory
                .selectFrom(user)
                .join(user.oauthAccounts, oAuthAccount).fetchJoin()
                .join(user.role).fetchJoin()
                .where(
                        oAuthAccount.provider.eq(provider)
                        .and(oAuthAccount.providerUserId.eq(providerUserId))
                )
                .fetchOne();
        return Optional.ofNullable(resultUser);
    }


}