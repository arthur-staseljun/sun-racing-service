package org.sun.racing.exception;

public class RaceDurationValidationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Race duration shall be integer between 1 and 3600 seconds";
    public RaceDurationValidationException() {
        super(ERROR_MESSAGE);
    }
}
