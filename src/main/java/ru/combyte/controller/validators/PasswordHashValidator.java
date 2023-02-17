package ru.combyte.controller.validators;

import ru.combyte.enitities.User;

import java.util.regex.Pattern;

public class PasswordHashValidator {
    private final static int HASH_LENGTH = 64;
    private final static Pattern CHARACTERS_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");

    public static boolean isOkLength(User user) {
        return isOkLength(user.getPasswordHash());
    }
    public static boolean isOkLength(String passwordHash) {
        return passwordHash.length() == HASH_LENGTH;
    }

    public static boolean isOkCharacters(User user) {
        return isOkCharacters(user.getPasswordHash());
    }

    public static boolean isOkCharacters(String passwordHash) {
        return CHARACTERS_PATTERN.matcher(passwordHash).matches();
    }
}
