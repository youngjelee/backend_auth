package com.backend.auth.config.common.base;

import com.backend.auth.config.exception.ResponseCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(ResponseCode responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public static <T> ApiResponse<T> of(ResponseCode responseCode, T data) {
        return new ApiResponse<>(responseCode, data);
    }
}