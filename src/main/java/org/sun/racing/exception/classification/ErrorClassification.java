package org.sun.racing.exception.classification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public enum ErrorClassification implements ErrorClassifier, Serializable {
    INTERNAL_SERVER_ERROR("int-err-00", "Internal server error" ),
    RACE_DURATION_PARSE_ERROR("rc-dv-00", "Race duration shall be integer between 1 and 3600 seconds"),
    RACER_ID_PARSE_ERROR("rc-id-00", "Exception while parsing raceId"),
    PARTICIPANT_ALREADY_JOINED("reg-ex-00", "Participant already joined"),
    RACE_DOES_NOT_EXIST("reg-ex-01", "Race doesn't exist" ),
    RACE_IS_ACTIVE_OR_ALREADY_FINISHED("reg-ex-02", "Race is active or already finished" ),
    RACE_IS_NOT_ACTIVE("com-ex-00", "Race is not active"),
    PARTICIPANT_NOT_PARTICIPATING_IN_RACE("com-ex-01", "Participant is not participating in the Race"),
    PARTICIPANT_HAS_TOO_FEW_POINTS("com-ex-03", "Participant has too few points"),
    UNABLE_TO_UPDATE_THE_ENTITY("com-ex-04", "Was not able to update the entity, please retry after some time"),
    RACE_ALREADY_JOINED_EXCEPTION("com-ex-05", "Participant already joined the race");

    private static final long serialVersionUID = -6849794470754667710L;

    private final String errorCode;
    private final String errorMessage;

}
