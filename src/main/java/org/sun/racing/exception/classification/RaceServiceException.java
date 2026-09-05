package org.sun.racing.exception.classification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RaceServiceException extends RuntimeException {

    private final ErrorClassification errorClassification;

}
