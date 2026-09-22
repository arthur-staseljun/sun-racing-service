package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.sun.racing.model.Race;
import org.sun.racing.model.request.PostWinnersBoardRequest;
import org.sun.racing.persistance.ParticipationRepository;
import org.sun.racing.persistance.RaceRepository;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportingService {

    private final WebClient defaultReportingWebClient;
    private final WebClient fallbackReportingWebClient;
    private final RaceRepository raceRepository;
    private final ParticipationRepository participationRepository;

    public void reportWinners(UUID raceId) {
        RaceEntity raceEntity = raceRepository.findById(raceId)
                .orElseThrow(() -> new IllegalArgumentException("No race entity found with id: " + raceId));
        if (raceEntity.getRaceStatus() != Race.RaceStatus.FINISHED) {
            throw new IllegalArgumentException("Race " + raceId + " is not finished yet");
        }

        List<ParticipationEntity> participationEntities = participationRepository.getTop3ByRaceIdOrderByScoreDesc(raceId);
        PostWinnersBoardRequest request = new PostWinnersBoardRequest(raceId, participationEntities);
        defaultReportingWebClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(exception -> fallbackReportingWebClient.post()
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .retrieve()
                        .toBodilessEntity())
                .doOnError(error -> log.error("Error calling reporting services", error))
                .onErrorResume(error -> Mono.empty())
                .block();
    }
}
