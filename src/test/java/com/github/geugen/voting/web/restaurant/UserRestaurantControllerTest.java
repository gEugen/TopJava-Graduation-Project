package com.github.geugen.voting.web.restaurant;

import com.github.geugen.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.geugen.voting.util.RestaurantsUtil.*;
import static com.github.geugen.voting.web.restaurant.RestaurantTestData.*;
import static com.github.geugen.voting.web.user.UserTestData.USER2_MAIL;
import static com.github.geugen.voting.web.user.UserTestData.USER5_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserRestaurantController.REST_URL + '/';

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void getWithVoteMark() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT5_ID + "/with-vote-mark"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MARK_RESTAURANT_TO_MATCHER.contentJson(createTestVoteMarkUserRestaurantTo(restaurant5, VOTED)));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getAllWithVoteMark() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-vote-mark"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        VOTE_MARK_RESTAURANT_TO_MATCHER.contentJson(
                                createTestVoteMarkUserRestaurantTos(
                                        createTestVoteMarkUserRestaurantTo(restaurant1, NOT_VOTED),
                                        createTestVoteMarkUserRestaurantTo(restaurant2, NOT_VOTED),
                                        createTestVoteMarkUserRestaurantTo(restaurant3, VOTED),
                                        createTestVoteMarkUserRestaurantTo(restaurant4, NOT_VOTED),
                                        createTestVoteMarkUserRestaurantTo(restaurant5, NOT_VOTED))));
    }

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void getWithVoteMarkByNameAndAddress() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-vote-mark-by-name-and-address")
                .param("name", "NIAM-NIAM").param("city", "TOWN").param("street", "STREET").param("number", "22"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MARK_RESTAURANT_TO_MATCHER.contentJson(createTestVoteMarkUserRestaurantTo(restaurant5, VOTED)));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        USER_RESTAURANT_TO_MATCHER.contentJson(
                                createTestUserRestaurantTos(List.of(restaurant1, restaurant2, restaurant3, restaurant4, restaurant5))));
    }

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT5_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESTAURANT_TO_MATCHER.contentJson(createUserRestaurantTo(restaurant5)));
    }

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void getByNameAndAddress() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-name-and-address")
                .param("name", "NIAM-NIAM").param("city", "TOWN").param("street", "STREET").param("number", "22"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_RESTAURANT_TO_MATCHER.contentJson(createUserRestaurantTo(restaurant5)));
    }
}