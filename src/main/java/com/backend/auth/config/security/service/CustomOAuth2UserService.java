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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        OAuthAttributes attributes = OAuthProvider.getByRegistrationId(userRequest.getClientRegistration().getRegistrationId())
                                     .createAttributes( userRequest , oAuth2User );

//        OAuthAttributes attributes = OAuthAttributes.of( userRequest , oAuth2User );
        User user = processUser( attributes ) ;

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())), // 수정: GrantedAuthority 객체 사용
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User processUser(OAuthAttributes attributes ) {

        OAuthProvider provider = OAuthProvider.valueOf(attributes.getProvider().toUpperCase());
        // 기존 OAuthAccount가 있는지 조회 (provider와 providerUserId 기준)
        Optional<OAuthAccount> oAuthAccountOpt = oAuthAccountRepository.findByProviderAndProviderUserId(provider, attributes.getProviderUserId());

        if (oAuthAccountOpt.isPresent()) {
            // 기존 계정이 있으면 사용자 정보 업데이트
            User existingUser = oAuthAccountOpt.get().getUser();

            // 만약 User 엔티티에 업데이트 메서드가 있다면 아래처럼 호출합니다.
            // 예시: existingUser = existingUser.update(attributes.getName(), attributes.getPicture());
            // update 메서드가 없다면 필요한 필드를 수동으로 업데이트 하세요.
            // 예: existingUser.setNickname(attributes.getName());

            return userRepository.save(existingUser);
        } else {
            // 신규 사용자 생성
            // username은 고유값으로 email 또는 "provider_providerUserId" 등으로 구성할 수 있음
            String username = attributes.getEmail();
            String nickname = attributes.getName();
            User newUser = User.builder()
                    .username(username)
                    .nickname(nickname)
                    .password("") // OAuth 로그인이므로 비밀번호는 비워두거나 랜덤값 할당
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
