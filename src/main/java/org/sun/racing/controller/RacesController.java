package org.sun.racing.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.exception.RaceIdParseException;
import org.sun.racing.model.Race;
import org.sun.racing.model.request.CreateRaceRequest;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.service.RacingTransactionalService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/races")
public class RacesController {

    private final RacingTransactionalService racingTransactionalService;

    @PostMapping
    public Race createRace(@RequestBody CreateRaceRequest request) {
        int duration;
        try {
            duration = Integer.parseInt(request.getDurationInSeconds());
        } catch (NumberFormatException e) {
            throw new RaceDurationValidationException();
        }
        return racingTransactionalService.createNewRace(duration);
    }

    @PostMapping("/{raceIdString}/join")
    public ParticipationInfoResponse joinRace(@PathVariable String raceIdString,
                                              @RequestHeader("X-User-ID") @NotNull String participantId) {
        UUID raceId;
        try {
            raceId = UUID.fromString(raceIdString);
        } catch (RuntimeException ex) {
            throw new RaceIdParseException();
        }
        return racingTransactionalService.joinRace(raceId, participantId);
    }
}
