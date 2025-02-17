package com.backend.auth.config.security.oauth.provider;
import java.util.Map;

public interface OAuth2UserInfo {

    /**
     * OAuth2 제공자(플랫폼)에서 발급한 사용자 고유 식별자
     * 예) Google: sub, Kakao: id, Naver: id/response.id 등
     */
    String getProviderId();

    /**
     * 사용자 이름 혹은 닉네임
     */
    String getName();

    /**
     * 사용자 이메일 주소
     */
    String getEmail();

    /**
     * 프로필 이미지 URL (필요 없는 경우 생략 가능)
     */
    String getImageUrl();

    /**
     * 원본 attributes (필요하면 전체 응답을 확인하기 위해 제공)
     */
    Map<String, Object> getAttributes();
}
