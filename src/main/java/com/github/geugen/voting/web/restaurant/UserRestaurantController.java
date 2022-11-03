package com.github.geugen.voting.web.restaurant;


import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.RestaurantTo;
import com.github.geugen.voting.to.VoteMarkRestaurantTo;
import com.github.geugen.voting.util.RestaurantsUtil;
import com.github.geugen.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.geugen.voting.util.RestaurantsUtil.createUserRestaurantTos;
import static com.github.geugen.voting.util.RestaurantsUtil.createVoteMarkUserRestaurantTo;
import static com.github.geugen.voting.web.vote.UserVoteController.NOT_VOTED;
import static com.github.geugen.voting.web.vote.UserVoteController.VOTED;


@Tag(
        name = "User Restaurant Controller",
        description = "allows user to get restaurant list with current menu items or " +
                "specific restaurant on voting day by restaurant id or name with address, " +
                "to get same with marks of their vote")
@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
//@CacheConfig(cacheNames = "restaurant")
@AllArgsConstructor
public class UserRestaurantController {

    static final String REST_URL = "/api/user/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Operation(
            summary = "Get all restaurants with menu items by authorized user",
            description = "Returns restaurants with menu items")
    @GetMapping()
//    @Cacheable
    public List<RestaurantTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAll for user {}", authUserId);
        return createUserRestaurantTos(restaurantRepository.getAllWithMenuItems());
    }

    @Operation(
            summary = "Get restaurant with menu items by its id",
            description = "Returns found restaurant with menu items")
    @GetMapping("/{id}")
    public RestaurantTo get(@Parameter(description = "restaurant id") @PathVariable int id) {
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItems(id);
        log.info("get {}", id);
        return RestaurantsUtil.createUserRestaurantTo(restaurant);
    }

    @Operation(
            summary = "Get restaurant with menu items by name and address",
            description = "Returns found restaurant profile")
    @GetMapping("/by-name-and-address")
    public RestaurantTo get(
            @Parameter(description = "restaurant name") @RequestParam @NotBlank String name,
            @Parameter(description = "city name") @RequestParam @NotBlank String city,
            @Parameter(description = "street") @RequestParam @NotBlank String street,
            @Parameter(description = "building number") @RequestParam @NotNull Integer number) {
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItemsByNameAndAddress(name, city, street, number);
        log.info("get {} with [{}, {}, {}]", name, city, street, number);
        return RestaurantsUtil.createUserRestaurantTo(restaurant);
    }

    @Operation(
            summary = "Get restaurant list with menu items and vote marks for each one by user",
            description = "Returns restaurant list with menu items and vote marks")
    @GetMapping("/with-vote-mark")
    public List<VoteMarkRestaurantTo> getAllWithVoteMark(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAllWithVoteMark for user {}", authUserId);
        List<Restaurant> restaurants = restaurantRepository.getAllWithMenuItems();
        Vote vote = voteRepository.getVote(authUserId, LocalDate.now());
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        List<VoteMarkRestaurantTo> voteMarkRestaurantToList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
                voteMarkRestaurantToList.add(createVoteMarkUserRestaurantTo(restaurant, VOTED));
            } else {
                voteMarkRestaurantToList.add(createVoteMarkUserRestaurantTo(restaurant, NOT_VOTED));
            }
        }
        return voteMarkRestaurantToList;
    }

    @Operation(
            summary = "Get restaurant with menu items and vote mark by user",
            description = "Returns restaurant with menu items and vote mark")
    @GetMapping("/{id}/with-vote-mark")
    public VoteMarkRestaurantTo getWithVoteMark(
            @AuthenticationPrincipal AuthUser authUser, @Parameter(description = "restaurant id") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getWithVoteMark restaurant {} for user {}", id, authUserId);
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItems(id);
        Vote vote = voteRepository.getVote(authUserId, LocalDate.now());
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        VoteMarkRestaurantTo voteMarkRestaurantTo;
        if (votedRestaurantId != null && votedRestaurantId == id) {
            voteMarkRestaurantTo = createVoteMarkUserRestaurantTo(restaurant, VOTED);
        } else {
            voteMarkRestaurantTo = createVoteMarkUserRestaurantTo(restaurant, NOT_VOTED);
        }
        return voteMarkRestaurantTo;
    }

    @Operation(
            summary = "Get restaurant with menu items by name and address",
            description = "Returns restaurant with menu items")
    @GetMapping("/with-vote-mark-by-name-and-address")
    public VoteMarkRestaurantTo getWithVoteMarkByNameAndAddress(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "restaurant name") @RequestParam @NotBlank String name,
            @Parameter(description = "city name") @RequestParam @NotBlank String city,
            @Parameter(description = "street") @RequestParam @NotBlank String street,
            @Parameter(description = "building number") @RequestParam @NotNull Integer number) {
        int authUserId = authUser.id();
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItemsByNameAndAddress(name, city, street, number);
        log.info("getWithVoteMarkByNameAndAddress {} with [{}, {}, {}] for user {}", name, city, street, number, authUserId);
        Vote vote = voteRepository.getVote(authUserId, LocalDate.now());
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        VoteMarkRestaurantTo voteMarkRestaurantTo;
        if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
            voteMarkRestaurantTo = createVoteMarkUserRestaurantTo(restaurant, VOTED);
        } else {
            voteMarkRestaurantTo = createVoteMarkUserRestaurantTo(restaurant, NOT_VOTED);
        }
        return voteMarkRestaurantTo;
    }
}
