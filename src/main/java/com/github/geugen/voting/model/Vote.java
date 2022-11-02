package com.github.geugen.voting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(
        name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "vote_date"}, name = "uk_user_vote_date"),
        indexes = @Index(name = "restaurant_idx", columnList = "restaurant_id"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Column(name = "vote_date", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(pattern = "YYYY-MM-DD")
    private LocalDate voteDate;

    @Column(name = "vote_time", nullable = false, columnDefinition = "time default current_time")
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type = "date-time", pattern = "HH:mm:ss")
    private LocalTime voteTime;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Schema(hidden = true)
    private Restaurant restaurant;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(hidden = true)
    private User user;

    public Vote(Integer id, Restaurant restaurant, User user) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
    }

    public Vote(Integer id, LocalDate voteDate, LocalTime voteTime, Restaurant restaurant, User user) {
        super(id);
        this.voteDate = voteDate;
        this.voteTime = voteTime;
        this.restaurant = restaurant;
        this.user = user;
    }

    public Vote(Vote vote) {
        this(vote.id, vote.voteDate, vote.voteTime, vote.restaurant, vote.user);
    }

    @Override
    public String toString() {
        return "Vote:" + id + "[Restaurant: " + restaurant.getId() + ", User: " + user.getId() + ']';
    }
}
