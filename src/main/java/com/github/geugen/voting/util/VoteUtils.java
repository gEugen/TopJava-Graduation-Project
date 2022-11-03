package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.SaveVoteTo;
import com.github.geugen.voting.to.UserVoteTo;


public class VoteUtils {

    public static UserVoteTo createUserVoteTo(Vote vote) {
        return new UserVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant());
    }

    public static SaveVoteTo createSaveVoteTo(Vote vote) {
        return new SaveVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant().getId(), vote.getUser().getId());
    }
}