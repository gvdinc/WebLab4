package ru.combyte.controller.validators;

import lombok.NonNull;
import ru.combyte.enitities.Shot;

public class ShotValidator {
    /**
     * @throws NullPointerException if shot is null
     */
    public static boolean isXOk(@NonNull Shot shot) {
        return shot.getX() != null &&
                -5 < shot.getX() && shot.getX() < 3;
    }

    /**
     * @throws NullPointerException if shot is null
     */
    public static boolean isYOk(@NonNull Shot shot) {
        return shot.getY() != null &&
                -3 < shot.getY() && shot.getY() < 5;
    }

    /**
     * @throws NullPointerException if shot is null
     */
    public static boolean isScopeOk(@NonNull Shot shot) {
        return shot.getScope() != null &&
                -5 < shot.getScope() && shot.getScope() < 3;
    }
}
