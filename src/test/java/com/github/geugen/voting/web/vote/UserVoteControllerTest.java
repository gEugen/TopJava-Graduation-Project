package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.List;

import static com.github.geugen.voting.service.VoteService.setEndVoteChangeTime;
import static com.github.geugen.voting.util.RestaurantsUtil.createTo;
import static com.github.geugen.voting.util.RestaurantsUtil.createTos;
import static com.github.geugen.voting.web.restaurant.RestaurantTestData.*;
import static com.github.geugen.voting.web.user.UserTestData.*;
import static com.github.geugen.voting.web.vote.VoteTestData.RESTAURANT_TO_MATCHER;
import static com.github.geugen.voting.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER5_MAIL)
    void getWithVoteMark() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT5_ID + "/with-dishes-and-vote"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(restaurant5, VOTED)));
    }

    @Test
    @WithUserDetails(value = USER2_MAIL)
    void getAllWithVoteMark() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(
                        RESTAURANT_TO_MATCHER.contentJson(
                                createTos(
                                        createTo(restaurant1, NOT_VOTED), createTo(restaurant2, NOT_VOTED),
                                        createTo(restaurant3, VOTED), createTo(restaurant4, NOT_VOTED),
                                        createTo(restaurant5, NOT_VOTED))));
    }

    @Test
    @WithUserDetails(value = USER6_MAIL)
    void validVoteWithCreation() throws Exception {
        setEndVoteChangeTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT2_ID))
                .andDo(print())
                .andExpect(status().isCreated());

        VOTE_SAVE_MATCHER.assertMatch(voteRepository.getVotesByRestaurant(RESTAURANT2_ID), List.of(user6Vote));
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void validVoteWithUpdating() throws Exception {
        setEndVoteChangeTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isOk());

        List<Vote> currentVotes = voteRepository.getVotesByRestaurant(RESTAURANT1_ID);
        List<Vote> previousVotes = voteRepository.getVotesByRestaurant(RESTAURANT3_ID);
        VOTE_MATCHER.assertMatch(currentVotes, getTestCurrentVotes());
        VOTE_MATCHER.assertMatch(previousVotes, getTestPreviousVotes());
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void nonValidVoteAfterTimeIsOver() throws Exception {
        setEndVoteChangeTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isForbidden());

        List<Vote> currentVotes = voteRepository.getVotesByRestaurant(RESTAURANT1_ID);
        List<Vote> previousVotes = voteRepository.getVotesByRestaurant(RESTAURANT3_ID);
        VOTE_MATCHER.assertMatch(currentVotes, List.of(user1Vote, adminVote));
        VOTE_MATCHER.assertMatch(previousVotes, List.of(user2Vote, user3Vote));
    }
}