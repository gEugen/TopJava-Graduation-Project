package com.github.geugen.voting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;


@Schema(title = "OutputSaveVoteTo - Output Vote/Re-Vote DTO for User Vote Controller")
@Value
@EqualsAndHashCode(callSuper = true)
public class OutputSaveVoteTo extends BaseTo {

    @Schema(type = "date", pattern = "yyyy:MM:dd")
    LocalDate voteDate;

    @Schema(type = "date-time", pattern = "HH:mm:ss")
    LocalTime voteTime;

    Integer restaurantId;

    Integer userId;

    public OutputSaveVoteTo(Integer id, LocalDate voteDate, LocalTime voteTime, Integer restaurantId, Integer userId) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }
}