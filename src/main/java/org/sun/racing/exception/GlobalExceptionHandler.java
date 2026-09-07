package org.sun.racing.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RaceServiceException.class)
    public ResponseEntity<ErrorResponse> handleRaceDurationValidationException(RaceServiceException exception) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                exception.getErrorClassification().getErrorCode(), exception.getErrorClassification().getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.internalServerError().body(new ErrorResponse(
                ErrorClassification.INTERNAL_SERVER_ERROR.getErrorCode(), exception.getMessage()));
    }

    private record ErrorResponse(String errorCode, String errorMessage) {}
}
