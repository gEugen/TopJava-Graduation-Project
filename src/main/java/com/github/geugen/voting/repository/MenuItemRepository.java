package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.geugen.voting.error.DataConflictException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT d FROM MenuItem d WHERE d.restaurant.id=:restaurantId and d.date=:date")
    List<MenuItem> getAll(int restaurantId, LocalDate date);

    @Query("SELECT d FROM MenuItem d WHERE d.id = :id and d.restaurant.id = :restaurantId")
    Optional<MenuItem> get(int id, int restaurantId);

    default MenuItem checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}