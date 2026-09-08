package org.sun.racing.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sun.racing.persistance.entity.ParticipationEntity;
import org.sun.racing.persistance.entity.RaceEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.sun.racing.util.Utils.DATETIME_FORMAT;

@Getter
@NoArgsConstructor
public class RaceInfoResponse {
    private UUID raceId;
    private int durationInSeconds;
    private String raceStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime startedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime finishedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime updatedAt;

    private List<ParticipationInfoResponse> participants;
    public RaceInfoResponse(RaceEntity race, List<ParticipationEntity> participationEntities) {
        this.raceId = race.getId();
        this.durationInSeconds = race.getDurationInSeconds();
        this.raceStatus = race.getRaceStatus().name();
        this.createdAt = race.getCreatedAt();
        this.startedAt = race.getStartedAt();
        this.finishedAt = race.getFinishedAt();
        this.updatedAt = race.getUpdatedAt();
        this.participants = participationEntities.stream().map(entity -> new ParticipationInfoResponse(
                entity.getRaceId(), entity.getParticipantId(), entity.getScore(), entity.isFreezed(),
                        entity.getUpdatedAt(), entity.getCreatedAt())).toList();
    }
}
