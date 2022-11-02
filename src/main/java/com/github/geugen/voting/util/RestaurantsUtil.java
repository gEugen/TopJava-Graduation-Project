package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.to.UserRestaurantTo;
import com.github.geugen.voting.to.VoteMarkUserRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;


public class RestaurantsUtil {

    public static List<AdminRestaurantTo> createAdminRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createAdminTo)
                .collect(Collectors.toList());
    }

    public static List<UserRestaurantTo> createUserRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createUserRestaurantTo)
                .collect(Collectors.toList());
    }

    public static List<VoteMarkUserRestaurantTo> createTestVoteMarkUserRestaurantTos(VoteMarkUserRestaurantTo... restaurantTos) {
        return Arrays.stream(restaurantTos)
                .sorted(Comparator.comparing(VoteMarkUserRestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static List<AdminRestaurantTo> createTestAdminRestaurantTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createAdminTo)
                .sorted(Comparator.comparing(AdminRestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static VoteMarkUserRestaurantTo createTestVoteMarkUserRestaurantTo(Restaurant restaurant, boolean vote) {
        return new VoteMarkUserRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                Optional.ofNullable(restaurant.getMenuItems()).orElse(new ArrayList<>()), vote);
    }

    public static VoteMarkUserRestaurantTo createVoteMarkUserRestaurantTo(Restaurant restaurant, boolean vote) {
        return new VoteMarkUserRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems(), vote);
    }

    public static AdminRestaurantTo createAdminTo(Restaurant restaurant) {
        return new AdminRestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }

    public static UserRestaurantTo createUserRestaurantTo(Restaurant restaurant) {
        return new UserRestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getMenuItems());
    }
}
