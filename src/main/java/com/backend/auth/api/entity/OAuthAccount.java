package com.backend.auth.api.entity;


import com.backend.auth.api.entity.common.BaseTimeEntity;
import com.backend.auth.config.security.oauth.enums.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oauth_accounts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"provider", "providerUserId"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // OAuth 제공자를 enum 타입으로 저장 (예: NAVER, KAKAO, GOOGLE, GITHUB)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider;

    // 해당 제공자로부터 받은 사용자 고유 식별자
    @Column(nullable = false)
    private String providerUserId;

    // 토큰 관련 정보는 Redis 등 별도의 스토리지에서 관리

    // User와 다대일 관계 (한 사용자가 여러 OAuth 계정을 가질 수 있음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}