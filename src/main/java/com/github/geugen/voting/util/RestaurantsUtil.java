package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.to.RestaurantTo;
import com.github.geugen.voting.to.VoteMarkRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;


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

    public static List<Restaurant> createTestAdminRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                .sorted(Comparator.comparing(Restaurant::getName))
                .collect(Collectors.toList());
    }

    public static VoteMarkRestaurantTo createTestVoteMarkUserRestaurantTo(Restaurant restaurant, boolean vote) {
        return new VoteMarkRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                Optional.ofNullable(restaurant.getMenuItems()).orElse(new ArrayList<>()), vote);
    }

    public static VoteMarkRestaurantTo createVoteMarkUserRestaurantTo(Restaurant restaurant, boolean vote) {
        return new VoteMarkRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems(), vote);
    }

    public static RestaurantTo createUserRestaurantTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems());
    }
}
