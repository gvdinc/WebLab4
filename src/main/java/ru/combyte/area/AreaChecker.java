package ru.combyte.area;

import lombok.NonNull;
import ru.combyte.enitities.Shot;

import java.util.Date;

public class AreaChecker {
    public static Shot shot(double x, double y, double scope, String login) {
        long calculationsBeginTime = System.nanoTime();
        var shot = new Shot();
        shot.setX(x);
        shot.setY(y);
        shot.setScope(scope);
        shot.setHit(checkHit(x, y, scope));
        shot.setDatetime(new Date());
        shot.setOwnerLogin(login);
        shot.setProcessingTimeNano(System.nanoTime() - calculationsBeginTime);
        return shot;
    }

    /**
     * @throws NullPointerException if shot is null
     * @throws IllegalArgumentException if x, y or scope is null
     */
    public static boolean checkHit(@NonNull Shot shot) {
        if (shot.getX() == null || shot.getY() == null || shot.getScope() == null) {
            throw new IllegalArgumentException("null values on important for checkHit fields");
        }
        return checkHit(shot.getX(), shot.getY(), shot.getScope());
    }

    public static boolean checkHit(double x, double y, double scope) {
        boolean isInLeftUp =
                x <= 0 && y >= 0;
        boolean isInLeftDown =
                x <= 0 && y <= 0;
        boolean isInRightDown =
                x >= 0 && y <= 0;

        boolean isInLeftUpCircle = isInLeftUp &&
                x*x + y*y <= scope*scope;
        boolean isInLeftDownSquare = isInLeftDown &&
                (-1)*scope <= x &&
                (-1)*scope <= y;
        boolean inInRightDownTriangle = isInRightDown &&
                (-1) * 0.5 * scope + 0.5 * x <= y;

        return isInLeftUpCircle || isInLeftDownSquare || inInRightDownTriangle;
    }
}
