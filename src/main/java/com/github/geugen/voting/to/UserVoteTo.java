package com.github.geugen.voting.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.util.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Schema(title = "UserVoteTo - DTO used by User Vote Controller")
@Value
@EqualsAndHashCode(callSuper = true)
public class UserVoteTo extends BaseTo {

    @DateTimeFormat(pattern = DateTimeUtil.DATE_PATTERN)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate voteDate;

    @DateTimeFormat(pattern = DateTimeUtil.TIME_PATTERN)
    @Schema(type = "date-time", pattern = "HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalTime voteTime;

    Restaurant restaurant;

    public UserVoteTo(Integer id, LocalDate voteDate, LocalTime voteTime, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurant = restaurant;
    }
}