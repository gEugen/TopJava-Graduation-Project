package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional(readOnly = true)
//@CacheConfig(cacheNames = "restaurant")
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    //    @Cacheable
    @Query(
            "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE SIZE(r.menuItems)=0 OR mi.registered=:requestDate ORDER BY r.name ASC")
    List<Restaurant> getAllWithMenuItems(LocalDate requestDate);

    @Query(
            "SELECT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE r.id=:id AND (SIZE(r.menuItems)=0 OR mi.registered=:requestedDate)")
    Restaurant getWithMenuItems(int id, LocalDate requestedDate);

    @Query(
            "SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems mi " +
                    "WHERE r.name=:name AND r.address.city=:city AND r.address.street=:street " +
                    "AND r.address.buildingNumber=:number AND (SIZE(r.menuItems)=0 OR mi.registered=:requestedDate)")
    Restaurant getWithMenuItemsByNameAndAddress(String name, String city, String street, int number, LocalDate requestedDate);

    @Query(
            "SELECT DISTINCT r FROM Restaurant r " +
                    "WHERE r.name=:name AND r.address.city=:city AND r.address.street=:street AND r.address.buildingNumber=:number")
    Restaurant getByNameAndAddress(String name, String city, String street, int number);

    default Restaurant getExistedWithMenuItems(int id, LocalDate requestedDate) {
        return ValidationUtil.checkExisted(getWithMenuItems(id, requestedDate), id);
    }

    default Restaurant getExistedWithMenuItemsByNameAndAddress(String name, String city, String street, int number, LocalDate requestDate) {
        return ValidationUtil.checkExisted(getWithMenuItemsByNameAndAddress(name, city, street, number, requestDate), name, city, street, number);
    }

    default Restaurant getExistedByNameAndAddress(String name, String city, String street, int number) {
        return ValidationUtil.checkExisted(getByNameAndAddress(name, city, street, number), name, city, street, number);
    }
}
