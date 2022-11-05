package com.github.geugen.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;


@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type = "date", pattern = "yyyy:MM:dd")
    LocalDate voteDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type = "date-time", pattern = "HH:mm:ss")
    LocalTime voteTime;

    Integer restaurantId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Integer userId;

    public VoteTo(Integer id, LocalDate voteDate, LocalTime voteTime, Integer restaurantId, Integer userId) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}