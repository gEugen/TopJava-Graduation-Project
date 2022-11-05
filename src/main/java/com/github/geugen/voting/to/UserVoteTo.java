package com.github.geugen.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.geugen.voting.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;


@Value
@EqualsAndHashCode(callSuper = true)
public class UserVoteTo extends BaseTo {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type = "date", pattern = "yyyy:MM:dd")
    LocalDate voteDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type = "date-time", pattern = "HH:mm:ss")
    LocalTime voteTime;

    Restaurant restaurant;

    public UserVoteTo(Integer id, LocalDate voteDate, LocalTime voteTime, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurant = restaurant;
    }
}