package com.backend.auth.config.security;

import com.backend.auth.config.jwt.JwtAuthenticationFilter;
import com.backend.auth.config.jwt.JwtTokenProvider;
import com.backend.auth.config.security.service.CustomOAuth2UserService;
import com.backend.auth.config.security.service.CustomOidcUserService;
import com.backend.auth.config.security.service.CustomUserDetailsService;
import com.backend.auth.api.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final RefreshTokenService refreshTokenService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 빈(Bean)으로 등록
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authManagerBuilder.build();
    }

    /**
     * SecurityFilterChain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JWT 필터 생성
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);

        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // httpBasic 인증 방식 사용 X
                .httpBasic(AbstractHttpConfigurer::disable)
                // 세션을 사용하지 않도록 설정 (STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인증/인가 규칙
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**", "/login/**", "/oauth2/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // Google 같은 oidc 케이스는
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .userInfoEndpoint(userInfo-> userInfo.oidcUserService(customOidcUserService))
                        .successHandler((request, response, authentication) -> {

                            //TODO : 성공시 핸들러 추후 처리 예정
//                            String accessToken = jwtTokenProvider.createToken(authentication);
//
//                            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
//
//                            String username = ((OAuth2User) authentication.getPrincipal()).getAttribute("email");
//
//
//                            refreshTokenService.saveRefreshToken(username, refreshToken);
//
//                            // 클라이언트에 토큰 전달 (헤더나 JSON 응답으로 전달)
//                            response.addHeader("Authorization", "Bearer " + accessToken);
//                            response.addHeader("Refresh-Token", refreshToken);

                        })
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}