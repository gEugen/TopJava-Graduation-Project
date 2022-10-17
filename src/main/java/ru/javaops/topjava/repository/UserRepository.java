package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    //    https://stackoverflow.com/a/46013654/548473
//    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT u FROM User u WHERE u.id=?1")
//    Optional<User> getWithRestaurant(int id);

    @Query("SELECT u FROM User u JOIN FETCH u.restaurant WHERE u.id=?1")
    Optional<User> getWithRestaurant(int id);

//    @Query("UPDATE VOTES FROM User u JOIN FETCH u.restaurant WHERE u.id=?1")
//    void vote(int id, int authUserId);

//    @Modifying
//    @Query("UPDATE User u SET u.restaurant = :restaurant where u.id = :id")
//    void updateRestaurant(@Param(value = "id") int id, @Param(value = "restaurant") Restaurant restaurant);
}