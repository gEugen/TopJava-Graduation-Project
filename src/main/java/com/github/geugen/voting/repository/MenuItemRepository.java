package com.github.geugen.voting.repository;

import com.github.geugen.voting.error.DataConflictException;
import com.github.geugen.voting.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.geugen.voting.util.validation.ValidationUtil.checkExisted;


@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Query("SELECT mi FROM MenuItem mi WHERE mi.restaurant.id=:restaurantId and mi.id = :id")
    Optional<MenuItem> get(int restaurantId, int id);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.registered=:requestedDate and mi.restaurant.id=:restaurantId")
    Optional<List<MenuItem>> getAll(LocalDate requestedDate, int restaurantId);

    default List<MenuItem> getAllExisted(LocalDate requestedDate, int restaurantId) {
        Optional<List<MenuItem>> list = getAll(requestedDate, restaurantId);
        return list.orElseGet(() -> checkExisted(null, restaurantId));
    }

    default MenuItem checkBelongAndGet(int restaurantId, int id) {
        return get(restaurantId, id).orElseThrow(
                () -> new DataConflictException("MenuItem id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}