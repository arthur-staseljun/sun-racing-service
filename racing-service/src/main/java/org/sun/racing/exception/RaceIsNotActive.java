package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceIsNotActive extends RaceServiceException {
    public RaceIsNotActive() {
        super(ErrorClassification.RACE_IS_NOT_ACTIVE);
    }
}
