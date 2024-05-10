package yjh.devtoon.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yjh.devtoon.common.response.ApiReponse;
import yjh.devtoon.common.utils.DateFormatter;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(DevtoonException.class)
    public ResponseEntity<ApiReponse> applicationHandler(final DevtoonException e) {
        log.error("Error occurs {}", e.toString());

        ErrorData errorData = new ErrorData(
                e.getErrorCode().getStatus().value(),
                DateFormatter.getCurrentDateTime(),
                e.getErrorCode().getMessage(),
                e.getDetailMessage()
        );

        return ResponseEntity.ok(ApiReponse.error(errorData));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiReponse> validationHandler(final MethodArgumentNotValidException e) {
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

        return ResponseEntity.ok(ApiReponse.error(errorData));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiReponse> InternalExceptionHandler(final Exception e) {
        log.error("Error occurs {}", e.toString());

        ErrorData errorData = new ErrorData(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                DateFormatter.getCurrentDateTime(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                e.getMessage()
        );

        return ResponseEntity.ok(ApiReponse.error(errorData));
    }

}
