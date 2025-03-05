package com.backend.ljy.auth.config.security.service;

import com.backend.ljy.auth.api.entity.OAuthAccount;
import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.api.entity.UserProfile;
import com.backend.ljy.auth.api.entity.enums.UserRole;
import com.backend.ljy.auth.api.repository.OAuthAccountRepository;
import com.backend.ljy.auth.api.repository.UserRepository;
import com.backend.ljy.auth.config.security.oauth.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;

    /**
     * oauth 를 통해 각 strategy 에 맞게 설정된 OAuthAttributes 클래스 인스턴스정보로 이벤트 처리 하는 메소드
     */

    public User findOrCreateUserByOAuthAttributes(OAuthAttributes attributes) {

        // 기존 OAuthAccount 조회 (provider와 providerUserId 기준)
        Optional<User> oAuthAccountOpt = userRepository.getUserByProviderAndProviderUserId(attributes.getProvider(), attributes.getProviderUserId());

        // 신규 사용자 처리 핸들러
        return oAuthAccountOpt.orElseGet(() -> this.createUserByOAuthAttributes(attributes));
    }


    private User createUserByOAuthAttributes(OAuthAttributes attributes) {

        String username = attributes.getEmail();
        String nickname = attributes.getName();

        User newUser = User.builder()
                .userid(attributes.getProvider() +"_"+attributes.getProviderUserId()) // NAVER_providerUserId 로 id 설정
                .nickname(nickname)
                .password("") // OAUTH/ OIDC  비밀번호는 우선 빈값으로 사용
                .role(List.of(UserRole.ROLE_USER))
                .status("ACTIVE")
                .build();
        // profile
        UserProfile userProfile = UserProfile.builder()
                .user(newUser)
                .fullName(nickname)
                .email(username)
                .build();

        // oAuth계정
        OAuthAccount oAuthAccount = OAuthAccount.builder()
                .provider(attributes.getProvider())
                .providerUserId(attributes.getProviderUserId())
                .user(newUser)
                .build();


        newUser.loadUserProfileAndOauthAccount(userProfile, oAuthAccount);

        newUser = userRepository.save(newUser);

        return newUser;

    }
}
