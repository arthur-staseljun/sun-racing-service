package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class ParticipantHasFewPoints extends RaceServiceException {
    public ParticipantHasFewPoints() {
        super(ErrorClassification.PARTICIPANT_HAS_TOO_FEW_POINTS);
    }
}
