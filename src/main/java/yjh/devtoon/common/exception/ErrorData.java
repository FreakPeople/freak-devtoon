package yjh.devtoon.common.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorData {
    private final LocalDateTime timestamp;
    private final String message;
    private final String detailMessage;

    public ErrorData(
            final LocalDateTime timestamp,
            final String message,
            final String detailMessage
    ) {
        this.timestamp = timestamp;
        this.message = message;
        this.detailMessage = detailMessage;
    }

}