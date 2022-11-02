package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.AdminVoteTo;
import com.github.geugen.voting.to.UserVoteTo;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.geugen.voting.util.UserUtil.getTo;


public class VoteUtils {

    public static List<AdminVoteTo> createTos(List<Vote> votes) {
        return votes.stream().map(VoteUtils::createAdminTo).collect(Collectors.toList());
    }

    public static UserVoteTo createTo(Vote vote) {
        return new UserVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), RestaurantsUtil.createAdminTo(vote.getRestaurant()));
    }

    public static AdminVoteTo createAdminTo(Vote vote) {
        return new AdminVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), RestaurantsUtil.createAdminTo(vote.getRestaurant()), getTo(vote.getUser()));
    }
}
