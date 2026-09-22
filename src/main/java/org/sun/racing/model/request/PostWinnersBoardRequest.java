package org.sun.racing.model.request;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.sun.racing.persistance.entity.ParticipationEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Getter
public class PostWinnersBoardRequest {

    private UUID raceId;
    private List<WinnerBoardParticipant> winners;

    public PostWinnersBoardRequest(UUID raceId, List<ParticipationEntity> participationEntities) {
        this.raceId = raceId;
        this.winners = getWinners(participationEntities);
    }

    private static @NonNull List<WinnerBoardParticipant> getWinners(List<ParticipationEntity> participationEntities) {
        return IntStream.range(0, participationEntities.size())
                .mapToObj(i -> {
                    ParticipationEntity entity = participationEntities.get(i);
                    return new WinnerBoardParticipant(i + 1, entity.getParticipantId(), entity.getScore());
                }).toList();
    }

    private record WinnerBoardParticipant(int rank, String userId, int score) {
    }
}
