package ru.combyte.controller.validators;

import lombok.NonNull;
import ru.combyte.enitities.json.received.UserJson;

import java.util.regex.Pattern;

public class LoginValidator {
    private final static int MIN_LENGTH = 5;
    private final static int MAX_LENGTH = 20;
    private final static Pattern CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z_]*$");

    public static boolean isOkLength(@NonNull UserJson user) {
        return isOkLength(user.getLogin());
    }

    public static boolean isOkLength(String login) {
        return MIN_LENGTH <= login.length() && login.length() <= MAX_LENGTH;
    }

    public static boolean isOkCharacters(@NonNull UserJson user) {
        return isOkCharacters(user.getLogin());
    }

    public static boolean isOkCharacters(String login) {
        return CHARACTERS_PATTERN.matcher(login).matches();
    }
}
