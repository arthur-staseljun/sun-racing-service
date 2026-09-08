package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

import java.io.Serializable;

public class RaceIdParseException extends RaceServiceException implements Serializable {
    public RaceIdParseException() {
        super(ErrorClassification.RACER_ID_PARSE_ERROR);
    }
}
