package org.sun.racing.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.sun.racing.util.Utils.DATETIME_FORMAT;

@Getter
public class ParticipationInfoResponse {

    private UUID raceId;
    private String participantId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer score;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isFreezed;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime createdAt;

    public ParticipationInfoResponse(UUID raceId, String participantId, ZonedDateTime createdAt) {
        this.raceId = raceId;
        this.participantId = participantId;
        this.createdAt = createdAt;
    }

    public ParticipationInfoResponse(UUID raceId, String participantId, Integer score, boolean isFreezed,
                                     ZonedDateTime updatedAt, ZonedDateTime createdAt) {
        this.raceId = raceId;
        this.participantId = participantId;
        this.score = score;
        this.isFreezed = isFreezed;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
