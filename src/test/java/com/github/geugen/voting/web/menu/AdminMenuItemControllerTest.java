package com.github.geugen.voting.web.menu;


import com.github.geugen.voting.model.MenuItem;
import com.github.geugen.voting.repository.MenuItemRepository;
import com.github.geugen.voting.util.JsonUtil;
import com.github.geugen.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.geugen.voting.web.restaurant.RestaurantTestData.*;
import static com.github.geugen.voting.web.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminMenuItemControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuItemController.REST_URL + '/';

    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, RESTAURANT5_ID))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuItemTestData.DISH_MATCHER.contentJson(MenuItemTestData.MENU_ITEM_10, MenuItemTestData.MENU_ITEM_11));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MenuItemTestData.DISH11_ID, RESTAURANT5_ID))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuItemTestData.DISH_MATCHER.contentJson(MenuItemTestData.MENU_ITEM_11));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MenuItemTestData.DISH4_ID, RESTAURANT2_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.get(RESTAURANT2_ID, MenuItemTestData.DISH4_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = MenuItemTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MenuItemTestData.DISH4_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MenuItemTestData.DISH_MATCHER.assertMatch(repository.getExisted(MenuItemTestData.DISH4_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = MenuItemTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)));

        MenuItem created = MenuItemTestData.DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MenuItemTestData.DISH_MATCHER.assertMatch(created, newMenuItem);
        MenuItemTestData.DISH_MATCHER.assertMatch(repository.getExisted(newId), newMenuItem);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MenuItemTestData.DISH1_ID, RESTAURANT3_ID))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteUnauth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MenuItemTestData.DISH1_ID, RESTAURANT2_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuItem invalid = new MenuItem(null, "", 200);
        perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuItem invalid = new MenuItem(MenuItemTestData.DISH1_ID, "Scrambled eggs", 0);
        perform(MockMvcRequestBuilders.put(REST_URL + MenuItemTestData.DISH1_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        MenuItem invalid = new MenuItem(MenuItemTestData.DISH1_ID, "<script>alert(123)</script>", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + MenuItemTestData.DISH1_ID, RESTAURANT2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() {
        MenuItem invalid = new MenuItem(MenuItemTestData.DISH5_ID, "Scrambled eggs", 90);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + MenuItemTestData.DISH5_ID, RESTAURANT2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() {
        MenuItem invalid = new MenuItem(null, "Scrambled eggs", 90);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL, RESTAURANT2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }
}