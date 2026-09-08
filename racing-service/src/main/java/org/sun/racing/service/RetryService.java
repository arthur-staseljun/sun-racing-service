package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.persistance.entity.ParticipationEntity;

@Slf4j
@Service
@EnableResilientMethods
@RequiredArgsConstructor
public class RetryService {

    public final DriveService driveService;

    @Retryable(includes = ObjectOptimisticLockingFailureException.class,
            maxRetries = 10,
            delay = 100,
            jitter = 417,
            multiplier = 2,
            maxDelay = 10_000)
    public ParticipationInfoResponse drive(ParticipationEntity participation, int score) {
        return driveService.updateParticipation(participation, score);
    }
}
