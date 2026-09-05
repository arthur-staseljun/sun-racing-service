package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceDurationValidationException extends RaceServiceException {
    public RaceDurationValidationException() {
        super(ErrorClassification.RACE_DURATION_PARSE_ERROR);
    }
}
