package yjh.devtoon.common.exception;

import lombok.Getter;

@Getter
public class ErrorData<T> {

    private final int status;
    private final String timestamp;
    private final String message;
    private final T detailMessage;

    public ErrorData(
            final int status,
            final String timestamp,
            final String message,
            final T detailMessage
    ) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.detailMessage = detailMessage;
    }

}