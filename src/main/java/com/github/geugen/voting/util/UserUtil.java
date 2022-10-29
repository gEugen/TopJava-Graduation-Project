package com.github.geugen.voting.util;

import com.github.geugen.voting.config.SecurityConfiguration;
import com.github.geugen.voting.model.Role;
import com.github.geugen.voting.model.User;
import lombok.experimental.UtilityClass;
import com.github.geugen.voting.to.UserTo;


@UtilityClass
public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(SecurityConfiguration.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}