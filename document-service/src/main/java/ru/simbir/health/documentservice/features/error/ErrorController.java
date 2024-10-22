package ru.simbir.health.documentservice.features.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.documentservice.features.error.dto.ExceptionBody;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionBody> handleResponseStatusException(ResponseStatusException ex) {
        ExceptionBody responseBody = new ExceptionBody(ex.getReason(), "error");
        return ResponseEntity.status(ex.getStatusCode()).body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ExceptionBody("Validation failed", "validation_error", errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(final Exception e) {
        logger.error("An error occurred: ", e);
        return new ExceptionBody(e.getMessage(), "internal_error");
    }
}
