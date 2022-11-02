package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.to.UserRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;


public class RestaurantsUtil {

    public static UserRestaurantTo createUserTo(Restaurant restaurant, boolean vote) {
        return new UserRestaurantTo(
                restaurant.getId(), restaurant.getName(), restaurant.getAddress(),
                Optional.ofNullable(restaurant.getMenuItems()).orElse(new ArrayList<>()),
                vote);
    }

    public static List<UserRestaurantTo> createTos(UserRestaurantTo... userRestaurantTos) {
        return Arrays.stream(userRestaurantTos)
                .sorted(Comparator.comparing(UserRestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static List<AdminRestaurantTo> createTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createAdminTo)
                .sorted(Comparator.comparing(AdminRestaurantTo::getName))
                .collect(Collectors.toList());
    }

    public static AdminRestaurantTo createAdminTo(Restaurant restaurant) {
        return new AdminRestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }
}
