package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class ParticipantIsNotParticipatingInRace extends RaceServiceException {
    public ParticipantIsNotParticipatingInRace() {
        super(ErrorClassification.PARTICIPANT_NOT_PARTICIPATING_IN_RACE);
    }
}
