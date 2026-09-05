package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceDoesNotExist extends RaceServiceException {
    public RaceDoesNotExist() {
        super(ErrorClassification.RACE_DOES_NOT_EXIST);
    }
}
