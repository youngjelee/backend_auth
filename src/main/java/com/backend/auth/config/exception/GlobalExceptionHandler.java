package com.backend.auth.config.exception;

import com.backend.auth.config.common.base.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {

    // BusinessException 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        ApiResponse<?> apiResponse = ApiResponse.of(responseCode, null);
        return new ResponseEntity<>(apiResponse, responseCode.getHttpStatus());
    }

    // AuthException 처리
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthException(AuthException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        ApiResponse<?> apiResponse = ApiResponse.of(responseCode, null);
        return new ResponseEntity<>(apiResponse, responseCode.getHttpStatus());
    }

    // 그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        ex.printStackTrace(); // 로깅 처리
        ApiResponse<?> apiResponse = ApiResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}