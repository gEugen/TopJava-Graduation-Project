package com.github.geugen.voting.util;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.to.VoteTo;

import java.util.List;
import java.util.stream.Collectors;


public class VoteUtils {

    public static List<VoteTo> createTos(List<Vote> votes) {
        return votes.stream().map(VoteTo::new).collect(Collectors.toList());
    }

    public static VoteTo createTo(Vote vote) {
        if (vote != null) {
            return new VoteTo(vote);
        }
        return null;
    }
}
