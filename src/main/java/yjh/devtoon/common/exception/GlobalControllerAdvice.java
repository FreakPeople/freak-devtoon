package yjh.devtoon.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yjh.devtoon.common.response.Response;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DevtoonException.class)
    public ResponseEntity<Response> applicationHandler(final DevtoonException e) {
        ErrorData errorData = new ErrorData(
                LocalDateTime.now(),
                e.getErrorCode().getMessage(),
                e.getDetailMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.error(errorData));
    }

}
