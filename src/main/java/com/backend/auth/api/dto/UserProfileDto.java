package com.backend.auth.api.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String userid;
    private String nickname;
    private String email;
    private String profileImage; // 선택 사항: 프로필 이미지 URL 등

}