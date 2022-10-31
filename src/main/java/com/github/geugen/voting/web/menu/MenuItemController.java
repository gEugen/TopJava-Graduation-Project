package com.github.geugen.voting.web.menu;

import com.github.geugen.voting.model.MenuItem;
import com.github.geugen.voting.repository.MenuItemRepository;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.service.MenuItemService;
import com.github.geugen.voting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = MenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "menu_item")
public class MenuItemController {
    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu_items";

    private final MenuItemService menuItemService;

    private final MenuItemRepository menuItemRepository;

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get actual menu item list for restaurant by its id", description = "Returns actual menu item list")
    @GetMapping()
    @Cacheable
    public List<MenuItem> getActualByRestaurant(@Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("get {}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return menuItemRepository.getAll(restaurantId, LocalDate.now());
    }

    @Operation(summary = "Get restaurant menu item list by restaurant id and date", description = "Returns menu item list")
    @GetMapping("/by-date")
    public List<MenuItem> getAllByRestaurantForGivenDay(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "request date") @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        log.info("get {}", restaurantId);
        restaurantRepository.getExisted(restaurantId);
        return menuItemRepository.getAll(restaurantId, requestDate);
    }

    @Operation(summary = "Get menu item for restaurant by its ides", description = "Returns response with menu item")
    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItem> get(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of menu item") @PathVariable int itemId) {
        log.info("get menu item {} for restaurant {}", itemId, restaurantId);
        menuItemRepository.checkBelong(itemId, restaurantId);
        return ResponseEntity.of(menuItemRepository.findById(itemId));
    }

    @Operation(summary = "Delete menu item for restaurant by its ides", description = "Deletes menu item")
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of menu item") @PathVariable int itemId) {
        log.info("delete menu item {} for restaurant {}", restaurantId, itemId);
        MenuItem menuItem = menuItemRepository.checkBelong(itemId, restaurantId);
        menuItemRepository.delete(menuItem);
    }

    @Operation(
            summary = "Update menuItem details for restaurant by its ides",
            description = "Updates and returns response with updated menu item")
    @PutMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "updated menu item") @Valid @RequestBody MenuItem menuItem,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId,
            @Parameter(description = "id of menu item") @PathVariable int itemId) {
        log.info("update menu item {} for restaurant {}", menuItem, restaurantId);
        ValidationUtil.assureIdConsistent(menuItem, itemId);
        menuItemRepository.checkBelong(itemId, restaurantId);
        menuItemService.save(menuItem, restaurantId);
    }

    @Operation(
            summary = "Create new menu item for restaurant by its id", description = "Creates new menu item and returns response with new menu item")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(
            @Parameter(description = "created menu item") @Valid @RequestBody MenuItem menuItem,
            @Parameter(description = "id of restaurant") @PathVariable int restaurantId) {
        log.info("create menu item {} for restaurant {}", menuItem, restaurantId);
        ValidationUtil.checkNew(menuItem);
        MenuItem created = menuItemService.save(menuItem, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{itemId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}