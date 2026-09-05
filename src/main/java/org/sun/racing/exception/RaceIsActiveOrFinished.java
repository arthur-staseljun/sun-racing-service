package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceIsActiveOrFinished extends RaceServiceException {
    public RaceIsActiveOrFinished() {
        super(ErrorClassification.RACE_IS_ACTIVE_OR_ALREADY_FINISHED);
    }
}
