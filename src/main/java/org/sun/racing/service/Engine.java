package org.sun.racing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
                .onErrorResume(exception -> Mono.just(new PointsResponse("0")));
        PointsResponse pointsResponse = scoreResult.block();
        if (pointsResponse == null) {
            return 0;
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
