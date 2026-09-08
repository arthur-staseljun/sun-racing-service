package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.sun.racing.model.response.ParticipationInfoResponse;

import java.util.UUID;

@Service
@EnableResilientMethods
@RequiredArgsConstructor
public class RetryService {

    public final DriveService driveService;
    @Retryable(includes = ObjectOptimisticLockingFailureException.class,
            maxRetries = 5,
            delay = 100,
            jitter = 417,
            multiplier = 2,
            maxDelay = 10_000)
    public ParticipationInfoResponse drive(UUID raceId, String participantId) {
        return driveService.drive(raceId, participantId);
    }
}
