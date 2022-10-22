package ru.javaops.topjava.to;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @DateTimeFormat(pattern = DateTimeUtil.DATE_PATTERN)
    LocalDate voteDate;

    @DateTimeFormat(pattern = DateTimeUtil.TIME_PATTERN)
    @Schema(type = "date-time", pattern = "HH:mm:ss")
    LocalTime voteTime;

    AdminRestaurantTo restaurant;

    User user;

    public VoteTo(Vote vote) {
        super(vote.getId());
        this.voteDate = vote.getVoteDate();
        this.voteTime = vote.getVoteTime();
        this.restaurant = new AdminRestaurantTo(vote.getRestaurant());
        this.user = vote.getUser();
    }
}
