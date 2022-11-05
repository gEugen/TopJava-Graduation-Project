package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.InputSaveVoteTo;
import com.github.geugen.voting.to.OutputSaveVoteTo;
import com.github.geugen.voting.to.UserVoteTo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class VoteUtils {

    public static List<OutputSaveVoteTo> createTestOutputVoteTos(List<Vote> restaurants) {
        return restaurants.stream().map(VoteUtils::createOutputVoteTo)
                .sorted(Comparator.comparing(OutputSaveVoteTo::getId))
                .collect(Collectors.toList());
    }

    public static UserVoteTo createUserVoteTo(Vote vote) {
        return new UserVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant());
    }

    public static OutputSaveVoteTo createOutputVoteTo(Vote vote) {
        return new OutputSaveVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurant().getId(), vote.getUser().getId());
    }

    public static OutputSaveVoteTo createFromInputSaveVoteTo(InputSaveVoteTo vote) {
        return new OutputSaveVoteTo(
                vote.getId(), vote.getVoteDate(), vote.getVoteTime(), vote.getRestaurantId(), vote.getUserId());
    }
}