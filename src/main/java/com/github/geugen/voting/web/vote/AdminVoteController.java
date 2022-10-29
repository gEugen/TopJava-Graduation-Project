package com.github.geugen.voting.web.vote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.VoteTo;

import java.util.List;

import static com.github.geugen.voting.util.VoteUtils.createTos;


@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminVoteController {

    static final String REST_URL = "/api/admin/vote/restaurant";

    private final VoteRepository voteRepository;

    @Operation(summary = "Get vote with user and restaurant details by vote id", description = "Returns vote")
    @GetMapping("/{id}")
    public VoteTo getVote(@Parameter(description = "id of vote") @PathVariable int id) {
        log.info("getVote");
        Vote vote = voteRepository.getExisted(id);
        return new VoteTo(vote);
    }

    @Operation(summary = "Get user votes for selected restaurant by its id", description = "Returns user votes for selected restaurant")
    @GetMapping("/{id}/user-votes")
    public List<VoteTo> getVotesByRestaurant(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("getWithUsersVotes");
        return createTos(voteRepository.getVotesByRestaurant(id));
    }

    @Operation(summary = "Get all user vote list", description = "Returns all user vote list")
    @GetMapping("/all-user-votes")
    public List<VoteTo> getAllVotes() {
        log.info("getAllWithUsersVotes");
        return createTos(voteRepository.findAll());
    }
}
