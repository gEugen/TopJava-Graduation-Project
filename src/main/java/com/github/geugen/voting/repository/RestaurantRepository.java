package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query(
            "SELECT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE r.id=?1 AND (SIZE(r.menuItems) = 0 OR mi.date = current_date)")
    Restaurant getWithDishes(int id);

    @Query(
            "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE SIZE(r.menuItems) = 0 OR mi.date = current_date ORDER BY r.name ASC, r.email ASC")
    List<Restaurant> getAllWithDishes();

    @Query("SELECT r FROM Restaurant r WHERE r.email = LOWER(:email)")
    Optional<Restaurant> findByEmailIgnoreCase(String email);
}
