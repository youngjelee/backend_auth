package com.backend.ljy.auth.config.exception;

import com.backend.ljy._config.exception.ResponseCode;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {
    private final ResponseCode responseCode;

    public AuthException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public AuthException(ResponseCode responseCode, String customMessage) {
        super(customMessage);
        this.responseCode = responseCode;
    }

    public ResponseCode getErrorCode() {
        return responseCode;
    }
}