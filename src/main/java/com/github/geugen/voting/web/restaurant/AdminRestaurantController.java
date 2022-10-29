package com.github.geugen.voting.web.restaurant;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.to.AdminRestaurantTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.geugen.voting.util.RestaurantsUtil.createTos;


@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
@AllArgsConstructor
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurant";

    private final RestaurantRepository restaurantRepository;

    private final RestaurantUniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    @Operation(summary = "Get restaurant profile list", description = "Returns restaurant profile list")
    @GetMapping()
    @Cacheable
    public List<AdminRestaurantTo> getAll() {
        log.info("getAll");
        return createTos(restaurantRepository.findAll());
    }

    @Operation(summary = "Get restaurant profile by its id", description = "Returns response with found restaurant profile")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }

    @Operation(summary = "Get restaurant profile by its e-mail", description = "Returns response with found restaurant profile")
    @GetMapping("/by-email")
    public ResponseEntity<Restaurant> getByEmail(
            @Parameter(description = "restaurant e-mail") @RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(restaurantRepository.findByEmailIgnoreCase(email));
    }

    @Operation(summary = "Delete restaurant profile by its id", description = "Delete restaurant profile")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @Operation(summary = "Update restaurant profile by its id", description = "Updates restaurant profile")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "updated restaurant profile") @Valid @RequestBody AdminRestaurantTo restaurantTo,
            @Parameter(description = "id of restaurant") @PathVariable int id) {
        log.info("update {}", id);
        ValidationUtil.assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = new Restaurant(restaurantTo);
        restaurant.setVote(restaurantRepository.getExisted(id).getVote());
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Create new restaurant profile", description = "Creates new restaurant profile and returns response with it")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(
            @Parameter(description = "created restaurant profile") @Valid @RequestBody AdminRestaurantTo restaurantTo) {
        log.info("create {}", restaurantTo);
        ValidationUtil.checkNew(restaurantTo);
        Restaurant created = restaurantRepository.save(new Restaurant(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
