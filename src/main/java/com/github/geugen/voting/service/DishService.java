package com.github.geugen.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.geugen.voting.model.Dish;
import com.github.geugen.voting.repository.DishRepository;
import com.github.geugen.voting.repository.RestaurantRepository;


@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return dishRepository.save(dish);
    }
}
