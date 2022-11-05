package com.github.geugen.voting.util.validation;


import com.github.geugen.voting.HasId;
import com.github.geugen.voting.error.DataConflictException;
import com.github.geugen.voting.error.IllegalRequestDataException;
import com.github.geugen.voting.model.Vote;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;


@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T> T checkExisted(T obj, int id) {
        if (obj == null) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
        return obj;
    }

    public static <T> T checkExisted(T obj, String name, String city, String street, int buildingNumber) {
        if (obj == null) {
            throw new IllegalRequestDataException(
                    "Entity with name= " + name + " and address= [" + city + ", " + street + ", " + buildingNumber + "] not found");
        }
        return obj;
    }

    public static <T> T checkExisted(T obj, LocalDate requestDate) {
        if (obj == null) {
            throw new IllegalRequestDataException("Entity for date=" + requestDate + " not found");
        }
        return obj;
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static Vote checkVote(Vote vote, int authUserId) {
        if (vote == null) {
            throw new DataConflictException("User with id=" + authUserId + " did not vote today");
        }
        return vote;
    }

    public static boolean checkReVoteTime(LocalTime voteTime, LocalTime endVoteChangeTime) {
        if (voteTime.isAfter(endVoteChangeTime)) {
            throw new DataConflictException("Vote change after 11.00 a.m. is not allowed");
        }
        return true;
    }

    public static void checkIdPresence(Integer id) {
        if (id == null) {
            throw new DataConflictException("Restaurant from request body must has id");
        }
    }

    public static LocalDate checkDateConsistent(LocalDate voteDate, LocalDate dateOfUpdatableDate, int id) {
        if (!dateOfUpdatableDate.isEqual(voteDate)) {
            throw new DataConflictException(
                    "Vote id= " + id + " from past date= " + dateOfUpdatableDate + " is not allowed to be updated");
        }
        return voteDate;
    }
}