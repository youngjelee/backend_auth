package com.backend.auth.api.controller;

import com.backend.auth.api.dto.LoginRequest;
import com.backend.auth.api.dto.SignupRequest;
import com.backend.auth.api.dto.TokenResponse;
import com.backend.auth.api.entity.User;
import com.backend.auth.api.entity.enums.UserRole;
import com.backend.auth.config.jwt.JwtTokenProvider;
import com.backend.auth.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * 로그인 -> JWT 토큰 발급
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // 인증 성공 시 SecurityContextHolder 에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // DB에서 사용자 정보 확인
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // JWT 생성
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // 이미 존재하는 username인지 체크
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        // 이미 존재하는 nickname인지 체크
        if (userRepository.findByUsername(request.getNickname()).isPresent()) {
            return ResponseEntity.badRequest().body("nickname is already taken");
        }


        // 비밀번호 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(request.getPassword());

        // 사용자 저장
        User user = new User(request.getUsername(), request.getNickname(), encodedPassword, UserRole.ROLE_USER);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(Authentication authentication) {
        // Authentication 객체에서 OAuth2 사용자 정보를 가져옵니다.
         OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // 예시: 사용자 이름(name) 속성을 가져와 모델에 추가
        String userName = oauth2User.getAttribute("name");

        // 필요에 따라 추가 정보를 모델에 넣을 수 있습니다.
        // 예) email, picture 등
        // String email = oauth2User.getAttribute("email");
        // model.addAttribute("email", email);

        // 로그인 성공 후 보여줄 뷰 이름을 반환합니다.
        return "loginSuccess"; // resources/templates/loginSuccess.html (Thymeleaf 사용시)와 연결
    }


}