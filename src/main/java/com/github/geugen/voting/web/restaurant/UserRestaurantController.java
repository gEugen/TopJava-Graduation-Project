package com.github.geugen.voting.web.restaurant;


import com.github.geugen.voting.model.Restaurant;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

import static com.github.geugen.voting.util.RestaurantsUtil.*;


@Tag(
        name = "User Restaurant Controller",
        description = "allows user to get restaurant list with current menu items or " +
                "specific restaurant on voting day by restaurant id or name with address, " +
                "to get same with marks of their vote")
@RestController
@RequestMapping(value = UserRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Validated
public class UserRestaurantController {

    static final String REST_URL = "/api/user/restaurants";

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Operation(
            summary = "Get all restaurants with menu items",
            description = "Returns restaurants with menu items")
    @GetMapping()
    public List<RestaurantTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        LocalDate requestDate = LocalDate.now();
        int authUserId = authUser.id();
        log.info("getAll for user {}", authUserId);
        return createUserRestaurantTos(restaurantRepository.getAllWithMenuItems(requestDate));
    }

    @Operation(
            summary = "Get restaurant with menu items by id",
            description = "Returns restaurant with menu items")
    @GetMapping("/{id}")
    public RestaurantTo get(@Parameter(description = "restaurant id") @PathVariable @Min(1) int id) {
        LocalDate requestedDate = LocalDate.now();
        log.info("get {}", id);
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItems(id, requestedDate);
        return RestaurantsUtil.createUserRestaurantTo(restaurant);
    }

    @Operation(
            summary = "Get restaurant with menu items by name and address",
            description = "Returns restaurant with menu items")
    @GetMapping("/by-name-and-address")
    public RestaurantTo getByNameAndAddress(
            @Parameter(description = "restaurant name") @RequestParam @NotBlank String name,
            @Parameter(description = "city name") @RequestParam @NotBlank String city,
            @Parameter(description = "street") @RequestParam @NotBlank String street,
            @Parameter(description = "building number") @RequestParam @Min(1) int number) {
        LocalDate requestDate = LocalDate.now();
        log.info("getByNameAndAddress {} with [{}, {}, {}]", name, city, street, number);
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItemsByNameAndAddress(name, city, street, number, requestDate);
        return RestaurantsUtil.createUserRestaurantTo(restaurant);
    }

    @Operation(
            summary = "Get restaurants with menu items and vote marks for each one",
            description = "Returns restaurants with menu items and vote marks")
    @GetMapping("/with-vote-mark")
    public List<VoteMarkRestaurantTo> getAllWithVoteMark(@AuthenticationPrincipal AuthUser authUser) {
        LocalDate requestDate = LocalDate.now();
        int authUserId = authUser.id();
        log.info("getAllWithVoteMark user {}", authUserId);
        return createVoteMarkRestaurantTos(
                restaurantRepository.getAllWithMenuItems(requestDate), voteRepository.getVote(authUserId, requestDate));
    }

    @Operation(
            summary = "Get restaurant with menu items and vote mark by id",
            description = "Returns restaurant with menu items and vote mark")
    @GetMapping("/{id}/with-vote-mark")
    public VoteMarkRestaurantTo getWithVoteMark(
            @AuthenticationPrincipal AuthUser authUser, @Parameter(description = "restaurant id") @PathVariable @Min(1) int id) {
        LocalDate requestDate = LocalDate.now();
        int authUserId = authUser.id();
        log.info("getWithVoteMark restaurant {} user {}", id, authUserId);
        return createVoteMarkRestaurantTo(
                restaurantRepository.getExistedWithMenuItems(id, requestDate), voteRepository.getVote(authUserId, requestDate), id);
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
            @Parameter(description = "building number") @RequestParam @Min(1) int number) {
        int authUserId = authUser.id();
        LocalDate requestDate = LocalDate.now();
        log.info("getWithVoteMarkByNameAndAddress {} with [{}, {}, {}] user {}", name, city, street, number, authUserId);
        Restaurant restaurant = restaurantRepository.getExistedWithMenuItemsByNameAndAddress(name, city, street, number, requestDate);
        return createVoteMarkRestaurantTo(restaurant, voteRepository.getVote(authUserId, requestDate), restaurant.getId());
    }
}
