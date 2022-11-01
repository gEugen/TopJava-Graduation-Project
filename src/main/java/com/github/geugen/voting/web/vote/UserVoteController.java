package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.service.VoteService;
import com.github.geugen.voting.to.VoteTo;
import com.github.geugen.voting.util.VoteUtils;
import com.github.geugen.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;


@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    static final String REST_URL = "/api/user/vote/restaurant";

    private final VoteService voteService;

    private final VoteRepository voteRepository;

    @Operation(summary = "Vote for restaurant selected by user", description = "Marks restaurant as voted one selected by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User vote was updated"),
            @ApiResponse(responseCode = "201", description = "New vote was created"),
            @ApiResponse(responseCode = "403", description = "Time is over for voting. Returns previous vote or null if no vote for current day"),
            @ApiResponse(responseCode = "422", description = "Unable to process vote")})
    @PostMapping("/{id}")
    public ResponseEntity<VoteTo> vote(@AuthenticationPrincipal AuthUser authUser, @Parameter(description = "id of " +
            "restaurant") @PathVariable int id) {
        int authUserId = authUser.id();
        Vote previousVote = voteRepository.getVote(authUserId);
        if (previousVote != null) {
            previousVote = new Vote(voteRepository.getVote(authUserId));
        }
        log.info("vote {} by user {}", id, authUserId);
        Vote vote = voteService.saveWithVote(previousVote, authUserId, id);
        LocalDate previousVoteDate = null;
        LocalTime previousVoteTime = null;
        if (previousVote != null) {
            previousVoteDate = previousVote.getVoteDate();
            previousVoteTime = previousVote.getVoteTime();
        }
        if (vote != null && (previousVote == null || previousVoteDate.isBefore(vote.getVoteDate()))) {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(vote.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(VoteUtils.createTo(vote));
        }
        if (vote != null && previousVoteDate.isEqual(vote.getVoteDate()) && previousVoteTime.isBefore(vote.getVoteTime())) {
            return ResponseEntity.ok(VoteUtils.createTo(vote));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(VoteUtils.createTo(previousVote));
    }
}
