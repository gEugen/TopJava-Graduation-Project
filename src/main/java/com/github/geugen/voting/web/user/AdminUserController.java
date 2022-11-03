package com.github.geugen.voting.web.user;

import com.github.geugen.voting.model.User;
import com.github.geugen.voting.util.validation.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Tag(
        name = "Admin User Controller",
        description = "allows administrator to get user profile list or " +
                "specific user profile by id or e-mail, create, update, delete, activate them")
@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
//@CacheConfig(cacheNames = "users")
public class AdminUserController extends AbstractUserController {

    static final String REST_URL = "/api/admin/users";

    @Operation(summary = "Get user profile by its id", description = "Returns response with user profile")
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@Parameter(description = "user id") @PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @Operation(summary = "Delete user profile by its id", description = "Delete user profile")
    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "user id") @PathVariable int id) {
        super.delete(id);
    }

    @Operation(summary = "Get all user profile list", description = "Returns all user profile list")
    @GetMapping
    public List<User> getAll() {
        log.info("getAll");
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @Operation(summary = "Create new user profile", description = "Creates new user profile and returns response with new user profile")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @CacheEvict(allEntries = true)
    public ResponseEntity<User> createWithLocation(@Parameter(description = "user profile") @Valid @RequestBody User user) {
        log.info("create {}", user);
        ValidationUtil.checkNew(user);
        User created = prepareAndSave(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update user profile", description = "Updates user profile")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @CacheEvict(allEntries = true)
    public void update(
            @Parameter(description = "updated user profile") @Valid @RequestBody User user,
            @Parameter(description = "user id") @PathVariable int id) {
        log.info("update {} with id={}", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        prepareAndSave(user);
    }

    @Operation(summary = "Get user profile by its e-mail", description = "Returns response with found user profile")
    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@Parameter(description = "user e-mail") @RequestParam String email) {
        log.info("getByEmail {}", email);
        return ResponseEntity.of(userRepository.findByEmailIgnoreCase(email));
    }

    @Operation(summary = "Enable/disable user profile by its e-mail", description = "Enable/disable user profile")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
//    @CacheEvict(allEntries = true)
    public void enable(
            @Parameter(description = "id of user") @PathVariable int id,
            @Parameter(description = "enable / disable flag") @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        User user = userRepository.getExisted(id);
        user.setEnabled(enabled);
    }
}