package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.UserVoteTo;


public class VoteUtils {

    public static UserVoteTo createTo(Vote vote) {
        return new UserVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), RestaurantsUtil.createAdminTo(vote.getRestaurant()));
    }
}
