package com.github.geugen.voting.repository;

import com.github.geugen.voting.error.DataConflictException;
import com.github.geugen.voting.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT mi FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId and mi.registered=:date")
    List<MenuItem> getAll(int restaurantId, LocalDate date);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.id = :id and mi.restaurant.id = :restaurantId")
    Optional<MenuItem> get(int id, int restaurantId);

    default MenuItem checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}