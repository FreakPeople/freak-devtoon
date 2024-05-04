package yjh.devtoon.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 4xx
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청사항을 찾지 못했습니다."),
    CONFLICT(HttpStatus.CONFLICT, "리소스가 이미 존재합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

}
