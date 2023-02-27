package ru.combyte.controller.utils;

import lombok.*;
import ru.combyte.controller.validators.LoginValidator;
import ru.combyte.controller.validators.PasswordValidator;
import ru.combyte.enitities.json.received.UserJson;

import java.util.LinkedList;
import java.util.List;

import static ru.combyte.Utils.getJsonProperty;

public class LoginControllerUtils {
    /**
     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
     */
    @SneakyThrows
    public static List<String> getCommandWithFullUserMissingKeys(@NonNull UserJson user) {
        var missingKeys = new LinkedList<String>();
        if (user.getLogin() == null) {
            missingKeys.add(getJsonProperty(user, "login"));
        }
        if (user.getPassword() == null) {
            missingKeys.add(getJsonProperty(user, "password"));
        }
        return missingKeys;
    }

    public static List<String> getWrongLengthValues(@NonNull UserJson user) {
        var wrongLengthValues = new LinkedList<String>();
        if (!LoginValidator.isOkLength(user)) {
            wrongLengthValues.add("login");
        }
        if (!PasswordValidator.isOkLength(user)) {
            wrongLengthValues.add("password");
        }
        return wrongLengthValues;
    }

    public static List<String> getWrongCharactersValues(UserJson user) {
        var wrongCharactersValues = new LinkedList<String>();
        if (!LoginValidator.isOkCharacters(user)) {
            wrongCharactersValues.add("login");
        }
        if (!PasswordValidator.isOkCharacters(user)) {
            wrongCharactersValues.add("password");
        }
        return wrongCharactersValues;
    }
}
