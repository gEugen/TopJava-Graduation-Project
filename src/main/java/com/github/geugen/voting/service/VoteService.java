package com.github.geugen.voting.service;

import com.github.geugen.voting.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.geugen.voting.model.Vote.END_VOTE_TIME;


@Service
@AllArgsConstructor
public class VoteService {

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    private final VoteRepository voteRepository;

    @Transactional
    public Vote saveWithVote(Vote previousVote, int authUserId, int id) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime time = localDateTime.toLocalTime();
        LocalDate date = localDateTime.toLocalDate();
        if (!time.isAfter(END_VOTE_TIME)) {
            if (previousVote == null || previousVote.getVoteDate().isBefore(date)) {
                return voteRepository.save(
                        new Vote(null, date, time, restaurantRepository.getExisted(id), userRepository.getExisted(authUserId)));
            } else {
                return voteRepository.saveAndFlush(
                        new Vote(previousVote.getId(), date, time, restaurantRepository.getReferenceById(id), userRepository.getReferenceById(authUserId)));
            }
        }
        return previousVote;
    }
}
