package com.github.geugen.voting.web.vote;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.InputSaveVoteTo;
import com.github.geugen.voting.to.OutputSaveVoteTo;
import com.github.geugen.voting.web.MatcherFactory;

import java.util.List;

import static com.github.geugen.voting.web.restaurant.RestaurantTestData.*;
import static com.github.geugen.voting.web.user.UserTestData.*;


public class VoteTestData {

    public static final MatcherFactory.Matcher<OutputSaveVoteTo> SAVE_VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(OutputSaveVoteTo.class, "voteDate", "voteTime");

    public static final int VOTE1_ID = 1;
    public static final int VOTE2_ID = 2;
    public static final int VOTE3_ID = 3;
    public static final int VOTE4_ID = 4;
    public static final int VOTE5_ID = 5;
    public static final int VOTE6_ID = 6;
    public static final int VOTE7_ID = 7;
    public static final int VOTE8_ID = 8;
    public static final int VOTE9_ID = 9;
    public static final int VOTE10_ID = 10;
    public static final int VOTE11_ID = 11;

    public static final Vote user1Vote = new Vote(VOTE1_ID, restaurant2, admin);
    public static final Vote user2Vote = new Vote(VOTE2_ID, restaurant2, user1);
    public static final Vote user3Vote = new Vote(VOTE3_ID, restaurant3, user4);
    public static final Vote user4Vote = new Vote(VOTE4_ID, restaurant5, admin);
    public static final Vote user5Vote = new Vote(VOTE5_ID, restaurant3, user3);
    public static final Vote user6Vote = new Vote(VOTE6_ID, restaurant1, user1);
    public static final Vote user7Vote = new Vote(VOTE7_ID, restaurant1, admin);
    public static final Vote user8Vote = new Vote(VOTE8_ID, restaurant3, user2);
    public static final Vote user9Vote = new Vote(VOTE9_ID, restaurant3, user3);
    public static final Vote user10Vote = new Vote(VOTE10_ID, restaurant5, user4);
    public static final Vote user11Vote = new Vote(VOTE11_ID, restaurant5, user5);

    public static final InputSaveVoteTo newVote = new InputSaveVoteTo(null, null, null, RESTAURANT3_ID, USER6_ID);
    public static final InputSaveVoteTo updatedVote = new InputSaveVoteTo(null, null, null, RESTAURANT5_ID, USER3_ID);

    public static List<Vote> getWithUpdatedVote() {
        return List.of(user10Vote, user11Vote, new Vote(VOTE9_ID, restaurant5, user3));
    }

    public static List<Vote> getWithoutUpdatedVote() {
        return List.of(user8Vote);
    }

    public static List<Vote> getNonValidWithoutUpdatedVote() {
        return List.of(user10Vote, user11Vote);
    }

    public static List<Vote> getNonValidWithUpdatedVote() {
        return List.of(user8Vote, user9Vote);
    }
}