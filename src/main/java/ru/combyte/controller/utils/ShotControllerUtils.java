package ru.combyte.controller.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import ru.combyte.controller.validators.ShotValidator;
import ru.combyte.enitities.Shot;

import java.util.LinkedList;
import java.util.List;

import static ru.combyte.Utils.getJsonProperty;

public class ShotControllerUtils {

//    /**
//     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
//     * @throws NullPointerException if user is null
//     */
//    public static List<String> getShotCommandStructureMissingKeys(@NonNull Shot shot) {
//        return getCommandWithClientShotMissingKeys(shot);
//    }
//
    /**
     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
     */
    @SneakyThrows
    public static List<String> getCommandWithClientShotMissingKeys(@NonNull Shot shot) {
        var missingKeys = new LinkedList<String>();
        if (shot.getX() == null) {
            missingKeys.add(getJsonProperty(shot, "x"));
        }
        if (shot.getY() == null) {
            missingKeys.add(getJsonProperty(shot, "y"));
        }
        if (shot.getScope() == null) {
            missingKeys.add(getJsonProperty(shot, "scope"));
        }
        return missingKeys;
    }


    /**
     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
     */
    public static List<String> getWrongValueValues(@NonNull Shot shot) {
        var wrongValueValues = new LinkedList<String>();
        if (!ShotValidator.isXOk(shot)) {
            wrongValueValues.add("x");
        }
        if (!ShotValidator.isYOk(shot)) {
            wrongValueValues.add("y");
        }
        if (!ShotValidator.isScopeOk(shot)) {
            wrongValueValues.add("R");
        }
        return wrongValueValues;
    }
}
