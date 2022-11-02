package com.github.geugen.voting.repository;

import com.github.geugen.voting.model.Vote;
import com.github.geugen.voting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1")
    Vote getVote(int authUserId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:authUserId AND v.voteDate=:voteDate")
    Vote getVote(int authUserId, LocalDate voteDate);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=:id")
    List<Vote> getVotesByRestaurant(int id);

    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=?1")
    void deleteByUserId(int id);

    default Vote getExisted(int authUserId) {
        return ValidationUtil.checkVote(getVote(authUserId), authUserId);
    }
}
