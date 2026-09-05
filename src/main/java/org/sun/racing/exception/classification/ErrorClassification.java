package org.sun.racing.exception.classification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public enum ErrorClassification implements ErrorClassifier, Serializable {
    RACE_DURATION_PARSE_ERROR("rc-dv-01", "Race duration shall be integer between 1 and 3600 seconds"),
    RACER_ID_PARSE_ERROR("rc-id-01", "Exception while parsing raceId");
    private static final long serialVersionUID = -6849794470754667710L;

    private final String errorCode;
    private final String errorMessage;

}
