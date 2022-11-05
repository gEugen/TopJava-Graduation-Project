package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.UserVoteTo;
import com.github.geugen.voting.to.VoteTo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class VoteUtils {

    public static List<VoteTo> createTestOutputVoteTos(List<Vote> restaurants) {
        return restaurants.stream().map(VoteUtils::createOutputVoteTo)
                .sorted(Comparator.comparing(VoteTo::getId))
                .collect(Collectors.toList());
    }

    public static UserVoteTo createUserVoteTo(Vote vote) {
        return new UserVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant());
    }

    public static VoteTo createOutputVoteTo(Vote vote) {
        return new VoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant().getId(), vote.getUser().getId());
    }

}