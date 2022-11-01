package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.to.UserRestaurantTo;

import java.util.*;
import java.util.stream.Collectors;


public class RestaurantsUtil {

    public static UserRestaurantTo createTo(Restaurant restaurant, boolean vote) {
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
        return restaurants.stream().map(AdminRestaurantTo::new)
                .sorted(Comparator.comparing(AdminRestaurantTo::getName))
                .collect(Collectors.toList());
    }

//    public static List<UserRestaurantTo> createUserRestaurantTos(List<Restaurant> restaurants) {
//        return restaurants.stream().map(UserRestaurantTo::new)
//                .sorted(Comparator.comparing(UserRestaurantTo::getName))
//                .collect(Collectors.toList());
//    }

    public static AdminRestaurantTo createTo(Restaurant restaurant) {
        return new AdminRestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getAddress());
    }
}
