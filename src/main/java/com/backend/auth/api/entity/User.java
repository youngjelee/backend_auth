package com.backend.auth.api.entity;

import com.backend.auth.api.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String password;

    // 사용자 역할을 enum으로 사용 (ROLE_ADMIN, ROLE_USER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // 계정 상태 (예: ACTIVE, SUSPENDED, INACTIVE)
    @Column(nullable = false)
    private String status = "ACTIVE";

    // UserProfile과 1:1 양방향 관계 (UserProfile에서 주도)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    // OAuth 계정과 1:N 양방향 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OAuthAccount> oauthAccounts;





    public User(String username, String nickname ,  String password, UserRole role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

}