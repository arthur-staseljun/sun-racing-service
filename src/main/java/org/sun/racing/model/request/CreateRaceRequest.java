package org.sun.racing.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class CreateRaceRequest {
    @Pattern(regexp = "\\d\\d*")
    private String durationInSeconds;
}
