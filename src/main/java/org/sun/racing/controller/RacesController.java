package org.sun.racing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sun.racing.model.Race;
import org.sun.racing.repository.RaceRepository;
import org.sun.racing.service.RaceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/races")
public class RacesController {

    private final RaceService raceService;
}
