package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.RestaurantTo;
import com.github.geugen.voting.to.VoteMarkRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.geugen.voting.web.vote.UserVoteController.NOT_VOTED;
import static com.github.geugen.voting.web.vote.UserVoteController.VOTED;


public class RestaurantsUtil {

    public static List<RestaurantTo> createUserRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createUserRestaurantTo)
                .collect(Collectors.toList());
    }

    public static List<VoteMarkRestaurantTo> createTestVoteMarkUserRestaurantTos(VoteMarkRestaurantTo... restaurantTos) {
        return Arrays.stream(restaurantTos)
                .sorted(Comparator.comparing(VoteMarkRestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static List<RestaurantTo> createTestUserRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createUserRestaurantTo)
                .sorted(Comparator.comparing(RestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static List<Restaurant> createTestAdminRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .collect(Collectors.toList());
    }

    public static List<VoteMarkRestaurantTo> createVoteMarkRestaurantTos(List<Restaurant> restaurants, Vote vote) {
        Integer votedRestaurantId = vote == null ? null : vote.getRestaurant().getId();
        return restaurants.stream()
                .map(
                        restaurant ->
                                (
                                        (votedRestaurantId != null) && votedRestaurantId.equals(restaurant.getId())) ?
                                        createVoteMarkRestaurant(restaurant, VOTED) :
                                        createVoteMarkRestaurant(restaurant, NOT_VOTED))
                .collect(Collectors.toList());
    }

    public static VoteMarkRestaurantTo createTestVoteMarkUserRestaurantTo(Restaurant restaurant, boolean vote) {
        return new VoteMarkRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                Optional.ofNullable(restaurant.getMenuItems()).orElse(new ArrayList<>()), vote);
    }

    public static VoteMarkRestaurantTo createVoteMarkRestaurant(Restaurant restaurant, boolean vote) {
        return new VoteMarkRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems(), vote);
    }

    public static RestaurantTo createUserRestaurantTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems());
    }

    public static VoteMarkRestaurantTo createVoteMarkRestaurantTo(Restaurant restaurant, Vote vote, int id) {
        Integer votedRestaurantId = vote == null ? null : vote.getRestaurant().getId();
        return (votedRestaurantId != null && votedRestaurantId.equals(id)) ?
                createVoteMarkRestaurant(restaurant, VOTED) : createVoteMarkRestaurant(restaurant, NOT_VOTED);
    }
}