package org.sun.racing.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.*;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.exception.RaceIdParseException;
import org.sun.racing.model.Race;
import org.sun.racing.model.request.CreateRaceRequest;
import org.sun.racing.model.response.ParticipationInfoResponse;
import org.sun.racing.model.response.RaceInfoResponse;
import org.sun.racing.service.AbilitiesService;
import org.sun.racing.service.DriveService;
import org.sun.racing.service.RacingService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/races")
public class RacesController {

    private final RacingService racingService;
    private final DriveService driveService;
    private final AbilitiesService abilitiesService;

    @PostMapping
    public Race createRace(@RequestBody CreateRaceRequest request) {
        int duration = parseDuration(request.getDurationInSeconds());
        return racingService.createNewRace(duration);
    }

    @PostMapping("/{raceIdString}/join")
    public ParticipationInfoResponse joinRace(@PathVariable String raceIdString,
                                              @RequestHeader("X-User-ID") @NotNull String participantId) {
        return racingService.joinRace(parse(raceIdString), participantId);
    }

    @PostMapping("/{raceIdString}/start")
    public Race joinRace(@PathVariable String raceIdString) {
        return racingService.startRace(parse(raceIdString));
    }

    @GetMapping("/{raceIdString}")
    public RaceInfoResponse getRace(@PathVariable String raceIdString) {
        return racingService.getRaceInfo(parse(raceIdString));
    }

    @PostMapping("/{raceIdString}/drive")
    public ParticipationInfoResponse drive(@PathVariable String raceIdString,
                                           @RequestHeader("X-User-ID") @NotNull String participantId) {
        return driveService.drive(parse(raceIdString), participantId);
    }

    @PostMapping("/{raceIdString}/abilities/oil-slick")
    public ParticipationInfoResponse oilSlick(@PathVariable String raceIdString,
                                              @RequestHeader("X-User-ID") @NotNull String participantId) {
        return abilitiesService.slickOil(parse(raceIdString), participantId);
    }

    @PostMapping("/{raceIdString}/abilities/engine-hack")
    public ParticipationInfoResponse hackEngine(@PathVariable String raceIdString,
                                                @RequestHeader("X-User-ID") @NotNull String participantId) {
        return abilitiesService.hackEngine(parse(raceIdString), participantId);
    }

    private static int parseDuration(String durationString) {
        int duration;
        try {
            duration = Integer.parseInt(durationString);
        } catch (NumberFormatException e) {
            throw new RaceDurationValidationException();
        }
        return duration;
    }

    private static @NonNull UUID parse(String raceIdString) {
        UUID raceId;
        try {
            raceId = UUID.fromString(raceIdString);
        } catch (RuntimeException ex) {
            throw new RaceIdParseException();
        }
        return raceId;
    }
}
