package com.backend.ljy.auth.config.security.service;

import com.backend.ljy.auth.api.entity.User;
import com.backend.ljy.auth.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userid));

        // Spring Security가 사용 가능한 형태(UserDetails)로 변환
        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                user.getPassword(),
                user.getRole().stream().map(v -> new SimpleGrantedAuthority(v.getValue())).toList()
        );
    }
}