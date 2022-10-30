package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.to.VoteRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;


public class RestaurantsUtil {

    public static VoteRestaurantTo createTo(Restaurant restaurant, boolean vote) {
        return new VoteRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getEmail(),
                Optional.ofNullable(restaurant.getMenuItems()).orElse(new ArrayList<>()),
                vote);
    }

    public static List<VoteRestaurantTo> createTos(VoteRestaurantTo... voteRestaurantTos) {
        return Arrays.stream(voteRestaurantTos)
                .sorted(Comparator.comparing(VoteRestaurantTo::getName).thenComparing(VoteRestaurantTo::getEmail))
                .collect(Collectors.toList());
    }

    public static List<AdminRestaurantTo> createTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(AdminRestaurantTo::new)
                .sorted(Comparator.comparing(AdminRestaurantTo::getName).thenComparing(AdminRestaurantTo::getEmail))
                .collect(Collectors.toList());
    }

    public static AdminRestaurantTo createTo(Restaurant restaurant) {
        return new AdminRestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getEmail());
    }
}
