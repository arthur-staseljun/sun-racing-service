package org.sun.racing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RaceDurationValidationException.class)
    public ResponseEntity<ErrorResponse> handleRaceDurationValidationException(RaceDurationValidationException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse("rc-dv-ex", exception.getMessage()));
    }

    private record ErrorResponse(String errorCode, String errorMessage) {}
}
