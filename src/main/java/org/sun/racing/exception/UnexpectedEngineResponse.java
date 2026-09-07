package org.sun.racing.exception;

import org.sun.racing.exception.classification.ErrorClassification;
import org.sun.racing.exception.classification.RaceServiceException;

public class UnexpectedEngineResponse extends RaceServiceException {
    public UnexpectedEngineResponse() {
        super(ErrorClassification.ENGINE_UNEXPECTED_RESPONSE);
    }
}
