package ru.combyte.controller.validators;

import lombok.NonNull;
import ru.combyte.enitities.json.received.UserJson;

import java.util.regex.Pattern;

public class PasswordValidator {
    private final static int MIN_LENGTH = 8;
    private final static int MAX_LENGTH = 30;
    private final static Pattern CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9_]*$");

    public static boolean isOkLength(@NonNull UserJson user) {
        return isOkLength(user.getPassword());
    }
    public static boolean isOkLength(String password) {
        return MIN_LENGTH <= password.length() && password.length() <= MAX_LENGTH;
    }

    public static boolean isOkCharacters(@NonNull UserJson user) {
        return isOkCharacters(user.getPassword());
    }

    public static boolean isOkCharacters(String password) {
        return CHARACTERS_PATTERN.matcher(password).matches();
    }
}
