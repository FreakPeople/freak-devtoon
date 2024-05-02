package yjh.devtoon.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yjh.devtoon.common.response.Response;
import yjh.devtoon.common.utils.DateFormatter;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DevtoonException.class)
    public ResponseEntity<Response> applicationHandler(final DevtoonException e) {
        log.error("Error occurs {}", e.toString());

        ErrorData errorData = new ErrorData(
                e.getErrorCode().getStatus().value(),
                DateFormatter.getCurrentDateTime(),
                e.getErrorCode().getMessage(),
                e.getDetailMessage()
        );

        return ResponseEntity.ok(Response.error(errorData));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> validationHandler(final MethodArgumentNotValidException e) {
        log.error("Error occurs {}", e.toString());

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> errors = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                .toList();

        ErrorData errorData = new ErrorData(
                HttpStatus.BAD_REQUEST.value(),
                DateFormatter.getCurrentDateTime(),
                e.getClass().getSimpleName(),
                errors
        );

        return ResponseEntity.ok(Response.error(errorData));
    }

}
