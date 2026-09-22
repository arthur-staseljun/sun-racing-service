package org.sun.racing.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.sun.racing.model.Race;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.sun.racing.util.Utils.DATETIME_FORMAT;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaderBoardResponse extends RaceInfoResponse {
    private UUID raceId;
    private int durationInSeconds;
    private String raceStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime startedAt;

    private List<LeaderBoardParticipant> leaderboard;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime updatedAt;
    public LeaderBoardResponse(RaceEntity race, List<ParticipationEntity> participationEntities) {
        this.raceId = race.getId();
        this.durationInSeconds = race.getDurationInSeconds();
        this.raceStatus = race.getRaceStatus().name();
        this.createdAt = race.getCreatedAt();
        this.startedAt = race.getStartedAt();
        this.updatedAt = race.getUpdatedAt();

        List<LeaderBoardParticipant> board = IntStream.range(0, participationEntities.size())
                .mapToObj(i -> {
                    ParticipationEntity entity = participationEntities.get(i);
                    return new LeaderBoardParticipant(i + 1, entity.getParticipantId(), entity.getScore());
                }).toList();
        this.leaderboard = board;
    }

    @Getter
    @RequiredArgsConstructor
    private class LeaderBoardParticipant {
        final int rank;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        final String racerId;
        final int score;
}
    }
