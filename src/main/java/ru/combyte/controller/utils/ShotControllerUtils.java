package ru.combyte.controller.utils;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import ru.combyte.controller.validators.ShotValidator;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;

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
        if (shot.getAreaID() == null) {
            missingKeys.add(getJsonProperty(shot, "areaID"));
        }
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
    @SneakyThrows
    public static List<String> getWrongValueValues(@NonNull Shot shot) {
        var wrongValueValues = new LinkedList<String>();
        if (!ShotValidator.isAreaIDOk(new AreaID(shot.getAreaID()))) {
            wrongValueValues.add(getJsonProperty(shot,"areaID"));
        }
        if (!ShotValidator.isXOk(shot)) {
            wrongValueValues.add(getJsonProperty(shot,"x"));
        }
        if (!ShotValidator.isYOk(shot)) {
            wrongValueValues.add(getJsonProperty(shot,"y"));
        }
        if (!ShotValidator.isScopeOk(shot)) {
            wrongValueValues.add(getJsonProperty(shot, "scope"));
        }
        return wrongValueValues;
    }

    /**
     * @return empty list, if hasn't. Return key-names as @JsonProperty value else
     */
    @SneakyThrows
    public static List<String> getCommandWithAreaIDMissingKeys(AreaID areaID) {
        var wrongValueValues = new LinkedList<String>();
        if (areaID.getAreaID() == null) {
            wrongValueValues.add(getJsonProperty(areaID,"areaID"));
        }
        return wrongValueValues;
    }
}
