package yjh.devtoon.common.exception;

import lombok.Getter;

@Getter
public class DevtoonException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    public DevtoonException(final ErrorCode errorCode, final String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

}