package yjh.devtoon.common.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorData<T> {
    private final LocalDateTime timestamp;
    private final String message;
    private final T detailMessage;

    public ErrorData(
            final LocalDateTime timestamp,
            final String message,
            final T detailMessage
    ) {
        this.timestamp = timestamp;
        this.message = message;
        this.detailMessage = detailMessage;
    }

}