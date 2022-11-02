package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.service.VoteService;
import com.github.geugen.voting.to.UserVoteTo;
import com.github.geugen.voting.util.validation.ValidationUtil;
import com.github.geugen.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.geugen.voting.util.VoteUtils.createTo;


@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final boolean CREATE = false;
    public static final boolean UPDATE = true;

    static final String REST_URL = "/api/user/vote/restaurant";

    private final VoteService voteService;

    private final VoteRepository voteRepository;

    @Operation(
            summary = "Get vote with restaurant and user own details by authorized user",
            description = "Returns vote with details")
    @GetMapping()
    public UserVoteTo get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getVote");
        Vote vote = voteRepository.getExisted(authUser.id());
        return createTo(vote);
    }

    @Operation(summary = "Primary vote for restaurant selected by user", description = "Return vote with details")
    @PostMapping()
    @Transactional
    public ResponseEntity<UserVoteTo> vote(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "vote dto") @Valid @RequestBody UserVoteTo voteTo) {
        ValidationUtil.checkNew(voteTo);
        Vote created = voteService.saveWithVote(voteTo, authUser.id(), CREATE);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createTo(created));
    }

    @Operation(summary = "Re-vote for restaurant selected by user", description = "Updates vote details")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void reVote(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "vote dto") @Valid @RequestBody UserVoteTo voteTo, @PathVariable int id) {
        ValidationUtil.assureIdConsistent(voteTo, id);
        voteService.saveWithVote(voteTo, authUser.id(), UPDATE);
    }
}
