package ru.simbir.health.accountservice.features.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.simbir.health.accountservice.features.error.dto.ExceptionBody;

@RestControllerAdvice
public class ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionBody> handleResponseStatusException(ResponseStatusException ex) {
        ExceptionBody responseBody = new ExceptionBody(ex.getReason(), "error");
        return ResponseEntity.status(ex.getStatusCode()).body(responseBody);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(final Exception e) {
        logger.error("An error occurred: ", e);
        return new ExceptionBody(e.getMessage(), "internal_error");
    }
}
