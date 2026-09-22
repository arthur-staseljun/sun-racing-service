package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceIsNotActiveException extends RaceServiceException {
    public RaceIsNotActiveException() {
        super(ErrorClassification.RACE_IS_NOT_ACTIVE);
    }
}
