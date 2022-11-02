package com.github.geugen.voting.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.AdminVoteTo;

import java.time.LocalDate;
import java.util.List;

import static com.github.geugen.voting.util.VoteUtils.createAdminTo;
import static com.github.geugen.voting.util.VoteUtils.createTos;


@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminVoteController {

    static final String REST_URL = "/api/admin/votes";

    private final VoteRepository voteRepository;

    @Operation(summary = "Get actual vote with user and restaurant details by user id", description = "Returns actual vote")
    @GetMapping("/by-user")
    public AdminVoteTo getVote(@Parameter(description = "user id") @RequestParam int userId) {
        log.info("getVote");
        Vote vote = voteRepository.getExisted(userId, LocalDate.now());
        return createAdminTo(vote);
    }

    @Operation(
            summary = "Get actual votes for selected restaurant by its id",
            description = "Returns actual votes for selected restaurant")
    @GetMapping("/votes-by-restaurant")
    public List<AdminVoteTo> getVotesByRestaurant(@Parameter(description = "restaurant id") @RequestParam int restaurantId) {
        log.info("getWithUsersVotes");
        return createTos(voteRepository.getVotesByRestaurant(restaurantId, LocalDate.now()));
    }

    @Operation(summary = "Get all actual vote list", description = "Returns all actual vote list")
    @GetMapping("/all-votes")
    public List<AdminVoteTo> getAllVotes() {
        log.info("getAllWithUsersVotes");
        return createTos(voteRepository.findAll());
    }
}
