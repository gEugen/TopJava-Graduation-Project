package com.github.geugen.voting.web.menu;

import com.github.geugen.voting.model.MenuItem;
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
import com.github.geugen.voting.repository.MenuItemRepository;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.service.MenuItemService;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "dishes")
public class MenuItemController {
    static final String REST_URL = "/api/admin/restaurant/{restaurantId}/dish";

    private final MenuItemService menuItemService;

    private final MenuItemRepository menuItemRepository;

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get dish list for restaurant by its id", description = "Returns dish list")
    @GetMapping()
    @Cacheable
    public List<MenuItem> getAllByRestaurantForCurrentDay(@Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("get {}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return menuItemRepository.getAll(restaurantId, LocalDate.now());
    }

    @Operation(summary = "Get dish for restaurant by its ides", description = "Returns response with dish")
    @GetMapping("/{dishId}")
    public ResponseEntity<MenuItem> get(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("get dish {} for restaurant {}", dishId, restaurantId);
        menuItemRepository.checkBelong(dishId, restaurantId);
        return ResponseEntity.of(menuItemRepository.findById(dishId));
    }

    @Operation(summary = "Delete dish for restaurant by its ides", description = "Deletes dish")
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of dish") @PathVariable int dishId) {
        log.info("delete {} for restaurant {}", restaurantId, dishId);
        MenuItem menuItem = menuItemRepository.checkBelong(dishId, restaurantId);
        menuItemRepository.delete(menuItem);
    }

    @Operation(
            summary = "Update menuItem details for restaurant by its ides",
            description = "Updates and returns response with updated menuItem")
    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "updated menuItem") @Valid @RequestBody MenuItem menuItem,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of menuItem") @PathVariable int dishId) {
        log.info("update {} for restaurant {}", menuItem, restaurantId);
        ValidationUtil.assureIdConsistent(menuItem, dishId);
        menuItemRepository.checkBelong(dishId, restaurantId);
        menuItemService.save(menuItem, restaurantId);
    }

    @Operation(
            summary = "Create new menuItem for restaurant by its id", description = "Creates new menuItem and returns response with new menuItem")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(
            @Parameter(description = "created menuItem") @Valid @RequestBody MenuItem menuItem,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("create {} for restaurant {}", menuItem, restaurantId);
        ValidationUtil.checkNew(menuItem);
        MenuItem created = menuItemService.save(menuItem, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}