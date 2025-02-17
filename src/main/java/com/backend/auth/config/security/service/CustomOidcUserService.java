package com.backend.auth.config.security.service;

import com.backend.auth.config.security.oauth.OAuthAttributes;
import com.backend.auth.api.entity.OAuthAccount;
import com.backend.auth.api.entity.User;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;
import com.backend.auth.api.entity.enums.UserRole;
import com.backend.auth.api.repository.OAuthAccountRepository;
import com.backend.auth.api.repository.UserRepository;
import com.backend.auth.config.security.oauth.service.OAuthAttributesConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        // 기본 OIDC 유저 정보 로드
        OidcUser oidcUser = super.loadUser(userRequest);

        //TODO:: 추후 oidcUser 내에서 claims , userInfo , idToken 필요시 추출후 사용하면됨 .

        OAuthAttributes attributes = OAuthProvider.getByRegistrationId(userRequest.getClientRegistration().getRegistrationId())
                                     .createAttributes( userRequest , oidcUser );
        // 사용자 존재 여부 확인 및 신규 생성 또는 업데이트
        User user = processUser(attributes);

        // OIDC 토큰 정보와 함께 사용자 권한 및 attribute를 설정하여 DefaultOidcUser 생성
        return new DefaultOidcUser(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                attributes.getNameAttributeKey()
        );
    }

    private User processUser(OAuthAttributes attributes ) {

        OAuthProvider provider = OAuthProvider.valueOf(attributes.getProvider().toUpperCase());
        // 기존 OAuthAccount 조회 (provider와 providerUserId 기준)
        Optional<OAuthAccount> oAuthAccountOpt = oAuthAccountRepository.findByProviderAndProviderUserId(provider, attributes.getProviderUserId());

        if (oAuthAccountOpt.isPresent()) {
            // 기존 사용자 업데이트 (필요한 경우 업데이트 로직 추가)
            User existingUser = oAuthAccountOpt.get().getUser();
            return userRepository.save(existingUser);
        } else {
            // 신규 사용자 생성
            String username = attributes.getEmail();
            String nickname = attributes.getName();
            User newUser = User.builder()
                    .username(username)
                    .nickname(nickname)
                    .password("") // OIDC 로그인이므로 비밀번호는 비워두거나 임의 값 사용
                    .role(UserRole.ROLE_USER)
                    .status("ACTIVE")
                    .build();
            newUser = userRepository.save(newUser);

            // 신규 OAuthAccount 생성
            OAuthAccount oAuthAccount = OAuthAccount.builder()
                    .provider(provider)
                    .providerUserId(attributes.getProviderUserId())
                    .user(newUser)
                    .build();
            oAuthAccountRepository.save(oAuthAccount);

            return newUser;
        }
    }
}