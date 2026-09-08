package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.sun.racing.exception.UnexpectedEngineResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Engine {

    private final WebClient defaultWebClient;
    private final WebClient fallbackWebClient;

    public int getScore() {
        Mono<PointsResponse> scoreResult = defaultWebClient.post()
                .retrieve()
                .bodyToMono(PointsResponse.class)
                .onErrorResume(exception -> fallbackWebClient.post()
                        .retrieve()
                        .bodyToMono(PointsResponse.class))
                .doOnError(exception -> {
                    throw new UnexpectedEngineResponse();
                });
        PointsResponse pointsResponse = scoreResult.block();
        if (pointsResponse == null) {
            throw new UnexpectedEngineResponse();
        }
        String points = pointsResponse.points();
        try {
            return Integer.parseInt(points);
        }  catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse point response");
        }
    }

    private record PointsResponse(String points){}
}
