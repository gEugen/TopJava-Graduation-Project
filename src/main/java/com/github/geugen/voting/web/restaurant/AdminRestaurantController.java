package com.github.geugen.voting.web.restaurant;


import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.util.RestaurantsUtil;
import com.github.geugen.voting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;


@Tag(
        name = "Admin Restaurant Controller",
        description = "allows administrator to get restaurant profile list " +
                "or specific restaurant profile with details, create, update, delete them")
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
//@CacheConfig(cacheNames = "restaurant")
@AllArgsConstructor
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get restaurant profile list", description = "Returns restaurant profile list")
    @GetMapping()
    public List<AdminRestaurantTo> getAll() {
        log.info("getAll");
        return RestaurantsUtil.createAdminRestaurantTos(restaurantRepository.findAll(Sort.by("name")));
    }

    @Operation(summary = "Get restaurant profile by its id", description = "Returns found restaurant profile")
    @GetMapping("/{id}")
    public AdminRestaurantTo get(@Parameter(description = "restaurant id") @PathVariable int id) {
        log.info("get {}", id);
        return RestaurantsUtil.createAdminTo(restaurantRepository.getExisted(id));
    }

    @Operation(summary = "Get restaurant profile by name and address", description = "Returns found restaurant profile")
    @GetMapping("/by-name-and-address")
    public AdminRestaurantTo get(
            @Parameter(description = "restaurant name") @RequestParam @NotBlank String name,
            @Parameter(description = "city name") @RequestParam @NotBlank String city,
            @Parameter(description = "street") @RequestParam @NotBlank String street,
            @Parameter(description = "building number") @RequestParam @NotNull Integer number) {
        Restaurant restaurant = restaurantRepository.getExistedByNameAndAddress(name, city, street, number);
        log.info("get {} with [{}, {}, {}]", name, city, street, number);
        return RestaurantsUtil.createAdminTo(restaurant);
    }

    @Operation(summary = "Delete restaurant profile by its id", description = "Delete restaurant profile")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @CacheEvict(allEntries = true)
    public void delete(@Parameter(description = "restaurant id") @PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @Operation(summary = "Update restaurant profile by its id", description = "Updates restaurant profile")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "restaurant profile") @Valid @RequestBody AdminRestaurantTo restaurantTo,
            @Parameter(description = "restaurant id") @PathVariable int id) {
        log.info("update {}", id);
        ValidationUtil.assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = new Restaurant(restaurantTo.getId(), restaurantTo.getName(), restaurantTo.getAddress());
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Create new restaurant profile", description = "Creates new restaurant profile and returns response with it")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(
            @Parameter(description = "restaurant profile") @Valid @RequestBody AdminRestaurantTo restaurantTo) {
        log.info("create {}", restaurantTo);
        ValidationUtil.checkNew(restaurantTo);
        Restaurant created = restaurantRepository.save(new Restaurant(restaurantTo.getId(), restaurantTo.getName(), restaurantTo.getAddress()));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}