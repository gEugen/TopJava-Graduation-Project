package com.github.geugen.voting.service;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.UserRepository;
import com.github.geugen.voting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


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
    public Vote saveWithVote(Vote previousVote, int authUserId, int id) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime time = localDateTime.toLocalTime();
        LocalDate date = localDateTime.toLocalDate();
        if (!time.isAfter(endVoteChangeTime)) {
            if (previousVote == null || previousVote.getVoteDate().isBefore(date)) {
                return voteRepository.save(
                        new Vote(null, date, time, restaurantRepository.getExisted(id), userRepository.getExisted(authUserId)));
            } else {
                return voteRepository.saveAndFlush(
                        new Vote(previousVote.getId(), date, time, restaurantRepository.getReferenceById(id), userRepository.getReferenceById(authUserId)));
            }
        }
        if (time.isAfter(endVoteChangeTime) && previousVote.getVoteDate().isBefore(date)) {
            return voteRepository.save(
                    new Vote(null, date, time, restaurantRepository.getExisted(id), userRepository.getExisted(authUserId)));
        }
        return previousVote;
    }
}
