package com.backend.auth.config.common.util;

import com.backend.auth.config.common.base.ApiResponse;
import com.backend.auth.config.exception.ResponseCode;

import com.backend.auth.config.common.base.ApiResponse;
import com.backend.auth.config.exception.ResponseCode;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    // 데이터가 있는 성공 응답 생성
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return new ResponseEntity<>(ApiResponse.of(ResponseCode.SUCCESS, data), ResponseCode.SUCCESS.getHttpStatus());
    }

    // 데이터가 없는 성공 응답 생성
    public static ResponseEntity<ApiResponse<?>> success() {
        return new ResponseEntity<>(ApiResponse.of(ResponseCode.SUCCESS, null), ResponseCode.SUCCESS.getHttpStatus());
    }

    // 에러 응답 생성
    public static ResponseEntity<ApiResponse<?>> error(ResponseCode responseCode) {
        return new ResponseEntity<>(ApiResponse.of(responseCode, null), responseCode.getHttpStatus());
    }

    // 커스텀 응답 생성 (코드와 데이터를 직접 지정)
    public static <T> ResponseEntity<ApiResponse<T>> response(ResponseCode responseCode, T data) {
        return new ResponseEntity<>(ApiResponse.of(responseCode, data), responseCode.getHttpStatus());
    }
}