package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.geugen.voting.web.restaurant.RestaurantTestData.RESTAURANT5_ID;
import static com.github.geugen.voting.web.user.UserTestData.ADMIN_MAIL;


public class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminVoteController.REST_URL + '/';

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getVote() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VoteTestData.user5Vote.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_GET_MATCHER.contentJson(VoteTestData.user5Vote));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getVotesByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT5_ID + "/user-votes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_GET_MATCHER.contentJson(VoteTestData.user4Vote, VoteTestData.user5Vote));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/all-user-votes"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_GET_MATCHER.contentJson(VoteTestData.user1Vote, VoteTestData.adminVote, VoteTestData.user2Vote, VoteTestData.user3Vote, VoteTestData.user4Vote, VoteTestData.user5Vote));
    }
}
