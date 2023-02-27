package ru.combyte.controller.validators;

import lombok.NonNull;
import ru.combyte.area.AreaChecker;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;

public class ShotValidator {
    public static boolean isXOk(@NonNull Shot shot) {
        return shot.getX() != null &&
                -5 < shot.getX() && shot.getX() < 3;
    }

    public static boolean isYOk(@NonNull Shot shot) {
        return shot.getY() != null &&
                -3 < shot.getY() && shot.getY() < 5;
    }

    public static boolean isScopeOk(@NonNull Shot shot) {
        return shot.getScope() != null &&
                -5 < shot.getScope() && shot.getScope() < 3;
    }

    public static boolean isAreaIDOk(@NonNull AreaID areaID) {
        return AreaChecker.supportedAreaCheckers.containsKey(areaID.getAreaID());
    }
}
