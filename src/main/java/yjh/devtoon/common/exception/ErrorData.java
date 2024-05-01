package yjh.devtoon.common.exception;

import lombok.Getter;

@Getter
public class ErrorData<T> {
    private final String timestamp;
    private final String message;
    private final T detailMessage;

    public ErrorData(
            final String timestamp,
            final String message,
            final T detailMessage
    ) {
        this.timestamp = timestamp;
        this.message = message;
        this.detailMessage = detailMessage;
    }

}