package org.sun.racing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.sun.racing.exception.classification.RaceServiceException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RaceServiceException.class)
    public ResponseEntity<ErrorResponse> handleRaceDurationValidationException(RaceServiceException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                exception.getErrorClassification().getErrorCode(), exception.getErrorClassification().getErrorMessage()));
    }

    private record ErrorResponse(String errorCode, String errorMessage) {}
}
