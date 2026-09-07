package org.sun.racing.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.sun.racing.util.Utils.DATETIME_FORMAT;

@Getter
@AllArgsConstructor
public class ParticipationInfoResponse {

    private UUID raceId;
    private String participantId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private ZonedDateTime createdAt;
}
