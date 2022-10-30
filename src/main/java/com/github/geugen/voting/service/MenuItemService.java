package com.github.geugen.voting.service;

import com.github.geugen.voting.model.MenuItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.geugen.voting.repository.MenuItemRepository;
import com.github.geugen.voting.repository.RestaurantRepository;


@Service
@AllArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuItem save(MenuItem menuItem, int restaurantId) {
        menuItem.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        return menuItemRepository.save(menuItem);
    }
}
