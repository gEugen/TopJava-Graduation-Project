package com.github.geugen.voting.service;

import com.github.geugen.voting.model.Address;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.repository.RestaurantRepository;
import com.github.geugen.voting.repository.UserRepository;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.to.AdminRestaurantTo;
import com.github.geugen.voting.to.UserVoteTo;
import com.github.geugen.voting.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    public Vote saveWithVote(UserVoteTo voteTo, int authUserId, boolean createOrUpdate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime voteTime = localDateTime.toLocalTime();
        LocalDate voteDate = localDateTime.toLocalDate();
        AdminRestaurantTo restaurant = voteTo.getRestaurant();
        Address address = restaurant.getAddress();
        Vote vote = null;
        if (createOrUpdate == CREATE) {
            vote = voteRepository.save(
                    new Vote(
                            null, voteDate, voteTime,
                            restaurantRepository.getExistedByNameAndAddress(
                                    restaurant.getName(), address.getCity(), address.getStreet(), address.getBuildingNumber()),
                            userRepository.getReferenceById(authUserId)));
        }
        if (createOrUpdate == UPDATE && ValidationUtil.checkReVoteTime(voteTime, endVoteChangeTime)) {
            vote = voteRepository.save(
                    new Vote(
                            voteTo.getId(), voteDate, voteTime,
                            restaurantRepository.getExistedByNameAndAddress(
                                    restaurant.getName(), address.getCity(), address.getStreet(), address.getBuildingNumber()),
                            userRepository.getReferenceById(authUserId)));
        }
        return vote;
    }
}
