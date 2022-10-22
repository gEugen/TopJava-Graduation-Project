package ru.javaops.topjava.web.vote;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.VoteRepository;
import ru.javaops.topjava.service.VoteService;
import ru.javaops.topjava.to.VoteRestaurantTo;
import ru.javaops.topjava.to.VoteTo;
import ru.javaops.topjava.util.VoteUtils;
import ru.javaops.topjava.web.AuthUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javaops.topjava.util.RestaurantsUtil.createTo;


@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {

    public static final boolean VOTED = true;
    public static final boolean NOT_VOTED = false;

    static final String REST_URL = "/api/profile/vote/restaurant";

    private final VoteService voteService;

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get restaurant with vote mark by user", description = "Returns restaurant with vote mark")
    @GetMapping("/{id}/with-dishes-and-vote")
    public VoteRestaurantTo getWithVoteMark(
            @AuthenticationPrincipal AuthUser authUser, @Parameter(description = "id of restaurant") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getWithVoteMark restaurant {} for user {}", id, authUserId);
        Restaurant restaurant = restaurantRepository.getWithDishes(id);
        Integer votedRestaurantId = voteRepository.getVote(authUserId).getRestaurant().getId();
        VoteRestaurantTo voteRestaurantTo;
        if (votedRestaurantId != null && votedRestaurantId == id) {
            voteRestaurantTo = createTo(restaurant, VOTED);
        } else {
            voteRestaurantTo = createTo(restaurant, NOT_VOTED);
        }
        return voteRestaurantTo;
    }

    @Operation(
            summary = "Get restaurant list with vote marks for each one by user",
            description = "Returns restaurant list with vote marks")
    @GetMapping()
    public List<VoteRestaurantTo> getAllWithVoteMark(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAllWithVoteMark for user {}", authUserId);
        List<Restaurant> restaurants = restaurantRepository.getAllWithDishes();
        Integer votedRestaurantId = voteRepository.getVote(authUserId).getRestaurant().getId();
        List<VoteRestaurantTo> voteRestaurantToList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
                voteRestaurantToList.add(createTo(restaurant, VOTED));
            } else {
                voteRestaurantToList.add(createTo(restaurant, NOT_VOTED));
            }
        }
        return voteRestaurantToList;
    }

    @Operation(summary = "Vote for restaurant selected by user", description = "Marks restaurant as voted one selected by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User vote was updated"),
            @ApiResponse(responseCode = "201", description = "New vote was created"),
            @ApiResponse(responseCode = "403", description = "Time is over for voting. Returns previous vote"),
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
