package com.github.geugen.voting.web.dish;

import com.github.geugen.voting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.geugen.voting.model.Dish;
import com.github.geugen.voting.repository.DishRepository;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.service.DishService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "dishes")
public class DishController {
    static final String REST_URL = "/api/admin/restaurant/{restaurantId}/dish";

    private final DishService dishService;

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get dish list for restaurant by its id", description = "Returns dish list")
    @GetMapping()
    @Cacheable
    public List<Dish> getAllByRestaurantForCurrentDay(@Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("get {}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return dishRepository.getAll(restaurantId, LocalDate.now());
    }

    @Operation(summary = "Get dish for restaurant by its ides", description = "Returns response with dish")
    @GetMapping("/{dishId}")
    public ResponseEntity<Dish> get(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("get dish {} for restaurant {}", dishId, restaurantId);
        dishRepository.checkBelong(dishId, restaurantId);
        return ResponseEntity.of(dishRepository.findById(dishId));
    }

    @Operation(summary = "Delete dish for restaurant by its ides", description = "Deletes dish")
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("delete {} for restaurant {}", restaurantId, dishId);
        Dish dish = dishRepository.checkBelong(dishId, restaurantId);
        dishRepository.delete(dish);
    }

    @Operation(
            summary = "Update dish details for restaurant by its ides",
            description = "Updates and returns response with updated dish")
    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "updated dish") @Valid @RequestBody Dish dish,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("update {} for restaurant {}", dish, restaurantId);
        ValidationUtil.assureIdConsistent(dish, dishId);
        dishRepository.checkBelong(dishId, restaurantId);
        dishService.save(dish, restaurantId);
    }

    @Operation(
            summary = "Create new dish for restaurant by its id", description = "Creates new dish and returns response with new dish")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(
            @Parameter(description = "created dish") @Valid @RequestBody Dish dish,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("create {} for restaurant {}", dish, restaurantId);
        ValidationUtil.checkNew(dish);
        Dish created = dishService.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}