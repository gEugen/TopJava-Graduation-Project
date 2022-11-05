package com.github.geugen.voting.service;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.UserRepository;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.VoteTo;
import com.github.geugen.voting.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.github.geugen.voting.util.validation.ValidationUtil.checkDateConsistent;
import static com.github.geugen.voting.util.validation.ValidationUtil.checkIdPresence;
import static com.github.geugen.voting.web.vote.UserVoteController.CREATE;
import static com.github.geugen.voting.web.vote.UserVoteController.UPDATE;


@Service
@AllArgsConstructor
public class VoteService {

    public static LocalTime endVoteChangeTime = LocalTime.of(11, 0);

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    public static void setEndVoteChangeTime(LocalTime endVoteChangeTime) {
        VoteService.endVoteChangeTime = endVoteChangeTime;
    }

    @Transactional
    public Vote saveWithVote(VoteTo voteTo, int authUserId, boolean createOrUpdate) {
        Integer voteToRestaurantId = voteTo.getRestaurantId();
        checkIdPresence(voteToRestaurantId);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime voteTime = localDateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS);
        LocalDate voteDate = localDateTime.toLocalDate();
        Vote vote = null;
        if (createOrUpdate == CREATE) {
            vote = voteRepository.save(
                    new Vote(
                            null, voteDate, voteTime,
                            restaurantRepository.getReferenceById(voteToRestaurantId),
                            userRepository.getReferenceById(authUserId)));
        }
        if (createOrUpdate == UPDATE && ValidationUtil.checkReVoteTime(voteTime, endVoteChangeTime)) {
            int voteId = voteTo.getId();
            vote = voteRepository.save(
                    new Vote(
                            voteId,
                            checkDateConsistent(
                                    voteDate, voteRepository.checkAndGetBelong(voteId, authUserId).getVoteDate(), voteId),
                            voteTime,
                            restaurantRepository.getReferenceById(voteToRestaurantId),
                            userRepository.getReferenceById(authUserId)));
        }
        return vote;
    }
}
