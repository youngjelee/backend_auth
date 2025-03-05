package com.backend.ljy._config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {

    // 성공 코드
    SUCCESS("2000", HttpStatus.OK, "요청이 성공하였습니다."),

    // 에러 코드
    INVALID_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("4001", HttpStatus.UNAUTHORIZED, "인증되지 않았습니다."),
    FORBIDDEN("4003", HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    NOT_FOUND("4004", HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),


    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),




    AUTH_TOKEN_EXPIRED("4002", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_CREDENTIALS("4005", HttpStatus.UNAUTHORIZED, "잘못된 인증 정보입니다.");

    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
