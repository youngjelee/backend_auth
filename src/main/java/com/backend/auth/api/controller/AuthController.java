package com.backend.auth.api.controller;

import com.backend.auth.api.dto.UserProfileDto;
import com.backend.auth.api.service.AuthService;
import com.backend.auth.config.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;


    /**
     * 로그인 -> JWT 토큰 발급
     */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
//        try {
//            // 사용자 인증 시도
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getUsername(),
//                            request.getPassword()
//                    )
//            );
//
//            // 인증 성공 시 SecurityContextHolder 에 저장
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // DB에서 사용자 정보 확인
//            User user = userRepository.findByUserid(request.getUsername())
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // JWT 생성
//            String token = jwtTokenProvider.createToken(user.getNickname(), user.getRole());
//
//            return ResponseEntity.ok(new TokenResponse(token));
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }

    /**
     * 회원가입
     */
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
//        // 이미 존재하는 username인지 체크
//        if (userRepository.findByUserid(request.getUsername()).isPresent()) {
//            return ResponseEntity.badRequest().body("Username is already taken");
//        }
//
//        // 이미 존재하는 nickname인지 체크
//        if (userRepository.findByUserid(request.getNickname()).isPresent()) {
//            return ResponseEntity.badRequest().body("nickname is already taken");
//        }
//
//
//        // 비밀번호 암호화
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(request.getPassword());
//
//        // 사용자 저장
//        User user = new User(request.getUsername(), request.getNickname(), encodedPassword, UserRole.ROLE_USER);
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully");
//    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(  HttpServletRequest request) {
        return authService.getProfileByAccessToken(request);
    }

    /**
     * 로그아웃 엔드포인트
     * HttpOnly 쿠키인 access_token을 만료시켜 삭제합니다.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout( HttpServletRequest request , HttpServletResponse response) {
        return authService.logout(request , response);
    }

}