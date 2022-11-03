package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.service.VoteService;
import com.github.geugen.voting.to.SaveVoteTo;
import com.github.geugen.voting.to.UserVoteTo;
import com.github.geugen.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

import static com.github.geugen.voting.util.VoteUtils.createSaveVoteTo;
import static com.github.geugen.voting.util.VoteUtils.createUserVoteTo;
import static com.github.geugen.voting.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.geugen.voting.util.validation.ValidationUtil.checkNew;


@Tag(
        name = "User Vote Controller",
        description = "allows user to vote for restaurants, change their own choice, and get the details of their vote ")
@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    public static final boolean CREATE = false;
    public static final boolean UPDATE = true;

    static final String REST_URL = "/api/user/votes";

    private final VoteService voteService;

    private final VoteRepository voteRepository;

    @Operation(
            summary = "Get own vote with restaurant and user details by id",
            description = "Returns vote with details")
    @GetMapping("/{id}")
    public UserVoteTo get(@AuthenticationPrincipal AuthUser authUser, @Parameter(description = "vote id") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getVote with id= {} by user= {}", id, authUserId);
        Vote vote = voteRepository.checkAndGetBelong(id, authUserId);
        return createUserVoteTo(vote);
    }

    @Operation(
            summary = "Get own vote with restaurant and user details by date",
            description = "Returns vote with details")
    @GetMapping("by-date")
    public UserVoteTo getByDate(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "request date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        int authUserId = authUser.id();
        log.info("getVote on date= {} by user= {}", requestDate, authUserId);
        Vote vote = voteRepository.getVote(authUserId, requestDate);
        return createUserVoteTo(vote);
    }

    @Operation(
            summary = "Get actual own vote with restaurant and user details",
            description = "Returns vote with details")
    @GetMapping()
    public UserVoteTo getActual(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getActual by user= {}", authUserId);
        Vote vote = voteRepository.getExisted(authUserId, LocalDate.now());
        return createUserVoteTo(vote);
    }

    @Operation(
            summary = "Primary vote for restaurant during the day",
            description = "Return vote with details")
    @PostMapping()
    public ResponseEntity<SaveVoteTo> vote(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "vote DTO") @Valid @RequestBody SaveVoteTo voteTo) {
        int authUserId = authUser.id();
        log.info("vote user= {}", authUserId);
        checkNew(voteTo);
        Vote created = voteService.saveWithVote(voteTo, authUserId, CREATE);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createSaveVoteTo(created));
    }

    @Operation(
            summary = "Re-vote for restaurant up to 11.00 a.m. inclusive",
            description = "Updates vote details")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void reVote(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "vote DTO") @Valid @RequestBody SaveVoteTo voteTo, @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("reVote user= {}", authUserId);
        assureIdConsistent(voteTo, id);
        voteService.saveWithVote(voteTo, authUserId, UPDATE);
    }
}
