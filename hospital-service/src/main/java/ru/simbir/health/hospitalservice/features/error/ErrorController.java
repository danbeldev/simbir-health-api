package ru.simbir.health.hospitalservice.features.error;

import lombok.RequiredArgsConstructor;
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
import ru.simbir.health.hospitalservice.features.error.dto.ExceptionBody;

import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionBody> handleResponseStatusException(ResponseStatusException ex) {
        ExceptionBody responseBody = new ExceptionBody(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(responseBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleValidationException(MethodArgumentNotValidException ex) {
        Optional<FieldError> first = ex.getBindingResult().getFieldErrors().stream().findFirst();
        String message = first.isPresent() ? first.get().getDefaultMessage() : "Validation failed";
        return new ExceptionBody(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(final Exception e) {
        logger.error("An error occurred: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
