package com.github.geugen.voting.repository;

import com.github.geugen.voting.error.DataConflictException;
import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:authUserId AND v.voteDate=:voteDate")
    Vote getVote(int authUserId, LocalDate voteDate);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.id=:id AND v.user.id=:authUserId")
    Optional<Vote> getVote(int id, int authUserId);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:id AND v.voteDate=:voteDate")
    List<Vote> getVotesByRestaurant(int id, LocalDate voteDate);

    Optional<Vote> findByUserIdAndVoteDate(int authUserId, LocalDate voteDate);

    default Vote getExisted(int userId, LocalDate voteDate) {
        return ValidationUtil.checkVote(getVote(userId, voteDate), userId);
    }

    default Vote checkAndGetBelong(int id, int userId) {
        return getVote(id, userId).orElseThrow(
                () -> new DataConflictException("Vote id=" + id + " doesn't belong to User id=" + userId));
    }
}
