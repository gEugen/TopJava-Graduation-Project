package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.util.validation.ValidationUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
@CacheConfig(cacheNames = "restaurant")
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Cacheable
    @Query(
            "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE SIZE(r.menuItems) = 0 OR mi.registered = current_date ORDER BY r.name ASC")
    List<Restaurant> getAllWithMenuItems();

    @Query(
            "SELECT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE r.id=?1 AND (SIZE(r.menuItems) = 0 OR mi.registered = current_date)")
    Restaurant getWithMenuItems(int id);

    @Query(
            "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems " +
                    "WHERE r.name=:name AND r.address.city=:city AND r.address.street=:street AND r.address.buildingNumber=:number")
    Restaurant getWithMenuItemsByNameAndAddress(String name, String city, String street, int number);

    @Query(
            "SELECT DISTINCT r FROM Restaurant r " +
                    "WHERE r.name=:name AND r.address.city=:city AND r.address.street=:street AND r.address.buildingNumber=:number")
    Restaurant getByNameAndAddress(String name, String city, String street, int number);

    default Restaurant getExistedWithMenuItems(int id) {
        return ValidationUtil.checkExisted(getWithMenuItems(id), id);
    }

    default Restaurant getExistedWithMenuItemsByNameAndAddress(String name, String city, String street, int number) {
        return ValidationUtil.checkExisted(getWithMenuItemsByNameAndAddress(name, city, street, number), name, city, street, number);
    }

    default Restaurant getExistedByNameAndAddress(String name, String city, String street, int number) {
        return ValidationUtil.checkExisted(getByNameAndAddress(name, city, street, number), name, city, street, number);
    }
}
