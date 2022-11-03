package com.github.geugen.voting.web.user;

import com.github.geugen.voting.model.User;
import com.github.geugen.voting.repository.UserRepository;
import com.github.geugen.voting.repository.VoteRepository;
import com.github.geugen.voting.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected VoteRepository voteRepository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    //    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        voteRepository.deleteByUserId(id);
        userRepository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return userRepository.save(UserUtil.prepareToSave(user));
    }
}