package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class ParticipantAlreadyJoinedException extends RaceServiceException {
    public ParticipantAlreadyJoinedException() {
        super(ErrorClassification.PARTICIPANT_ALREADY_JOINED);
    }
}
