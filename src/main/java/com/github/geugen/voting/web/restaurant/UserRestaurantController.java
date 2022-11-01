package com.github.geugen.voting.web.restaurant;


import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.UserRestaurantTo;
import com.github.geugen.voting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.github.geugen.voting.util.RestaurantsUtil.createTo;
import static com.github.geugen.voting.web.vote.UserVoteController.NOT_VOTED;
import static com.github.geugen.voting.web.vote.UserVoteController.VOTED;


@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurant")
@AllArgsConstructor
public class UserRestaurantController {

    static final String REST_URL = "/api/user/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Operation(
            summary = "Get restaurant list with menu items and vote marks for each one by user",
            description = "Returns restaurant list with menu items and vote marks")
    @GetMapping()
    public List<UserRestaurantTo> getAllWithVoteMark(@AuthenticationPrincipal AuthUser authUser) {
        int authUserId = authUser.id();
        log.info("getAllWithVoteMark for user {}", authUserId);
        List<Restaurant> restaurants = restaurantRepository.getAllWithMenuItems();
        Vote vote = voteRepository.getVote(authUserId);
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        List<UserRestaurantTo> userRestaurantToList = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
                userRestaurantToList.add(createTo(restaurant, VOTED));
            } else {
                userRestaurantToList.add(createTo(restaurant, NOT_VOTED));
            }
        }
        return userRestaurantToList;
    }

    @Operation(
            summary = "Get restaurant with menu items and vote mark by user",
            description = "Returns restaurant with menu items and vote mark")
    @GetMapping("/{id}/with-dishes-and-vote")
    public UserRestaurantTo getWithVoteMark(
            @AuthenticationPrincipal AuthUser authUser, @Parameter(description = "id of restaurant") @PathVariable int id) {
        int authUserId = authUser.id();
        log.info("getWithVoteMark restaurant {} for user {}", id, authUserId);
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItems(id);
        Vote vote = voteRepository.getVote(authUserId);
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        UserRestaurantTo userRestaurantTo;
        if (votedRestaurantId != null && votedRestaurantId == id) {
            userRestaurantTo = createTo(restaurant, VOTED);
        } else {
            userRestaurantTo = createTo(restaurant, NOT_VOTED);
        }
        return userRestaurantTo;
    }

    @Operation(summary = "Get restaurant with menu items by name and address", description = "Returns restaurant with menu items")
    @GetMapping("/by-name-and-address")
    public UserRestaurantTo getWithVoteMarkByNameAndAddress(
            @AuthenticationPrincipal AuthUser authUser,
            @Parameter(description = "restaurant name") @RequestParam @NotBlank String name,
            @Parameter(description = "city name") @RequestParam @NotBlank String city,
            @Parameter(description = "street") @RequestParam @NotBlank String street,
            @Parameter(description = "building number") @RequestParam @NotNull Integer number) {
        int authUserId = authUser.id();
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItemsByNameAndAddress(name, city, street, number);
        log.info("getWithVoteMarkByNameAndAddress {} with [{}, {}, {}] for user {}", name, city, street, number, authUserId);
        Vote vote = voteRepository.getVote(authUserId);
        Integer votedRestaurantId = null;
        if (vote != null) {
            votedRestaurantId = vote.getRestaurant().getId();
        }
        UserRestaurantTo userRestaurantTo;
        if (votedRestaurantId != null && votedRestaurantId.equals(restaurant.getId())) {
            userRestaurantTo = createTo(restaurant, VOTED);
        } else {
            userRestaurantTo = createTo(restaurant, NOT_VOTED);
        }
        return userRestaurantTo;
    }
}
