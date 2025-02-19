package com.backend.auth.api.entity;

import com.backend.auth.api.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile extends BaseTimeEntity {

    // User의 id를 공유 기본키로 사용
    @Id
    private Long userId;

    @OneToOne
    @MapsId  // User의 id를 PK로 사용하도록 매핑
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 사용자 전체 이름
    @Column(nullable = false)
    private String fullName;

    // 이메일
    private String email;

    // 전화번호 (선택 사항, unique)
    private String phone;

    // 생년월일
    private LocalDate dateOfBirth;

    // 프로필 이미지 URL 등 추가 필드
    private String profileImageUrl;
}