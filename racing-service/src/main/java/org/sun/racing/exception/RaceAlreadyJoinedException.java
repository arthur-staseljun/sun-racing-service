package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class RaceAlreadyJoinedException extends RaceServiceException {
    public RaceAlreadyJoinedException() {
        super(ErrorClassification.RACE_ALREADY_JOINED_EXCEPTION);
    }
}
