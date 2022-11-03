package com.github.geugen.voting.web.restaurant;

import com.github.geugen.voting.model.Restaurant;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.UserRepository;
import com.github.geugen.voting.util.JsonUtil;
import com.github.geugen.voting.util.RestaurantsUtil;
import com.github.geugen.voting.util.Util;
import com.github.geugen.voting.web.AbstractControllerTest;
import com.github.geugen.voting.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.geugen.voting.web.restaurant.RestaurantTestData.address;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_TO_MATCHER.contentJson(RestaurantsUtil.createTestAdminRestaurantTos(RestaurantTestData.restaurants)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.RESTAURANT1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RestaurantTestData.RESTAURANT_GET_MATCHER.contentJson(RestaurantTestData.restaurant1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RestaurantTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant aNew = RestaurantTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(aNew)));

        Restaurant created = RestaurantTestData.RESTAURANT_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        aNew.setId(newId);
        RestaurantTestData.RESTAURANT_TO_MATCHER.assertMatch(created, aNew);
        RestaurantTestData.RESTAURANT_TO_MATCHER.assertMatch(restaurantRepository.getExisted(newId), aNew);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "RESTAURANT", null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

//    @Test
//    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
//    void createDuplicate() throws Exception {
//        Restaurant invalid = new Restaurant(null, "YACITORIA");
//        perform(MockMvcRequestBuilders.post(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(invalid)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().string(containsString(RestaurantUniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
//    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = RestaurantTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RestaurantTestData.RESTAURANT_UPDATE_MATCHER.assertMatch(Util.initializeAndUnproxy(restaurantRepository.getExisted(RestaurantTestData.RESTAURANT1_ID)), RestaurantTestData.getUpdatedForCompare());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RestaurantTestData.RESTAURANT1_ID, "R", address);
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Restaurant invalid = new Restaurant(RestaurantTestData.RESTAURANT1_ID, "<script>alert(123)</script>", address);
        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

//    @Test
//    @Transactional(propagation = Propagation.NEVER)
//    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
//    void updateDuplicate() throws Exception {
//        Restaurant invalid =
//                new Restaurant(
//                        RestaurantTestData.RESTAURANT2_ID, "ASTORIA", new Address(null, "MOSCOW", "ARBAT", 10));
//        perform(MockMvcRequestBuilders.put(REST_URL + RestaurantTestData.RESTAURANT2_ID)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(invalid)))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().string(containsString(RestaurantUniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
//    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT3_ID))
                .andExpect(status().isNoContent());
        assertFalse(restaurantRepository.findById(RestaurantTestData.RESTAURANT3_ID).isPresent());
        UserTestData.USER_MATCHER.assertMatch(userRepository.getExisted(UserTestData.USER2_ID), UserTestData.user2);
    }

    @Test
    void deleteUnauth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RestaurantTestData.RESTAURANT2_ID))
                .andExpect(status().isUnauthorized());
    }
}
