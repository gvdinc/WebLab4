package ru.combyte.controller.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.combyte.controller.validators.LoginValidator;
import ru.combyte.controller.validators.PasswordHashValidator;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.User;

import java.util.LinkedList;
import java.util.List;

import static ru.combyte.Utils.getJsonProperty;

public class LoginControllerUtils {
    /**
     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
     */
    @SneakyThrows
    public static List<String> getCommandWithFullUserMissingKeys(@NonNull User user) {
        var missingKeys = new LinkedList<String>();
        if (user.getLogin() == null) {
            missingKeys.add(getJsonProperty(user, "login"));
        }
        if (user.getPasswordHash() == null) {
            missingKeys.add(getJsonProperty(user, "passwordHash"));
        }
        return missingKeys;
    }

    public static List<String> getWrongLengthValues(@NonNull User user) {
        var wrongLengthValues = new LinkedList<String>();
        if (!LoginValidator.isOkLength(user)) {
            wrongLengthValues.add("login");
        }
        if (!PasswordHashValidator.isOkLength(user)) {
            wrongLengthValues.add("password");
        }
        return wrongLengthValues;
    }

    public static List<String> getWrongCharactersValues(User user) {
        var wrongCharactersValues = new LinkedList<String>();
        if (!LoginValidator.isOkCharacters(user)) {
            wrongCharactersValues.add("login");
        }
        if (!PasswordHashValidator.isOkCharacters(user)) {
            wrongCharactersValues.add("password");
        }
        return wrongCharactersValues;
    }
}
