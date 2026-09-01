package org.sun.racing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sun.racing.exception.RaceDurationValidationException;
import org.sun.racing.model.Race;
import org.sun.racing.model.request.CreateRaceRequest;
import org.sun.racing.service.RaceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/races")
public class RacesController {

    private final RaceService raceService;

    @PostMapping
    public Race createRace(@RequestBody CreateRaceRequest request) {
        int duration;
        try {
            duration = Integer.parseInt(request.getDurationInSeconds());
        } catch (NumberFormatException e) {
            throw new RaceDurationValidationException();
        }
        return raceService.createNewRace(duration);
    }
}
