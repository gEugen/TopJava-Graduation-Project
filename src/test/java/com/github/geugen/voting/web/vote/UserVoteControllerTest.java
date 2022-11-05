package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.OutputSaveVoteTo;
import com.github.geugen.voting.util.JsonUtil;
import com.github.geugen.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.geugen.voting.service.VoteService.setEndVoteChangeTime;
import static com.github.geugen.voting.util.VoteUtils.*;
import static com.github.geugen.voting.web.restaurant.RestaurantTestData.RESTAURANT3_ID;
import static com.github.geugen.voting.web.restaurant.RestaurantTestData.RESTAURANT5_ID;
import static com.github.geugen.voting.web.user.UserTestData.*;
import static com.github.geugen.voting.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = UserVoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER6_MAIL)
    void vote() throws Exception {
        setEndVoteChangeTime(LocalTime.MAX);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());
        OutputSaveVoteTo created = SAVE_VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        OutputSaveVoteTo convertedNewVote = createFromInputSaveVoteTo(newVote);
        convertedNewVote.setId(newId);
        SAVE_VOTE_TO_MATCHER.assertMatch(created, convertedNewVote);
        SAVE_VOTE_TO_MATCHER.assertMatch(createOutputVoteTo(voteRepository.getVote(USER6_ID, LocalDate.now())), convertedNewVote);
    }


    @Test
    @WithUserDetails(value = USER3_MAIL)
    void reVote() throws Exception {
        setEndVoteChangeTime(LocalTime.MAX);
        LocalDate dateVote = LocalDate.now();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE9_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedVote)))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Vote> withUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT5_ID, dateVote);
        List<Vote> withoutUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT3_ID, dateVote);
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withUpdatedVote), createTestOutputVoteTos(getWithUpdatedVote()));
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withoutUpdatedVote), createTestOutputVoteTos(getWithoutUpdatedVote()));
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void nonValidReVoteAfterTimeIsOver() throws Exception {
        setEndVoteChangeTime(LocalTime.MIN);
        LocalDate dateVote = LocalDate.now();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE9_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedVote)))
                .andDo(print())
                .andExpect(status().isConflict());

        List<Vote> withUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT5_ID, dateVote);
        List<Vote> withoutUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT3_ID, dateVote);
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withUpdatedVote), createTestOutputVoteTos(getNonValidWithoutUpdatedVote()));
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withoutUpdatedVote), createTestOutputVoteTos(getNonValidWithUpdatedVote()));
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void reVoteWithNotOwn() throws Exception {
        setEndVoteChangeTime(LocalTime.MAX);
        LocalDate dateVote = LocalDate.now();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE10_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedVote)))
                .andDo(print())
                .andExpect(status().isConflict());

        List<Vote> withUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT5_ID, dateVote);
        List<Vote> withoutUpdatedVote = voteRepository.getVotesByRestaurant(RESTAURANT3_ID, dateVote);
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withUpdatedVote), createTestOutputVoteTos(getNonValidWithoutUpdatedVote()));
        SAVE_VOTE_TO_MATCHER.assertMatch(createTestOutputVoteTos(withoutUpdatedVote), createTestOutputVoteTos(getNonValidWithUpdatedVote()));
    }
}