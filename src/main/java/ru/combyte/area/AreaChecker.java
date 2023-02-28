package ru.combyte.area;

import lombok.NonNull;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class AreaChecker {
    public static Map<String, Predicate<Shot>> supportedAreaCheckers;

    static {
        supportedAreaCheckers = new HashMap<>();
        supportedAreaCheckers.put("Miron", AreaChecker::checkMironHit);
        supportedAreaCheckers.put("Vadim", AreaChecker::checkVadimHit);
    }

    /**
     * @throws UnsupportedOperationException if areaID is wrong
     */
    public static Shot shot(AreaID areaID, double x, double y, double scope, String login) throws UnsupportedOperationException {
        long calculationsBeginTime = System.nanoTime();
        if (!supportedAreaCheckers.containsKey(areaID.getAreaID())) {
            throw new UnsupportedOperationException("areaID is unsupported");
        }
        var shot = new Shot();
        shot.setX(x);
        shot.setY(y);
        shot.setScope(scope);
        shot.setAreaID(areaID.getAreaID());
        shot.setHit(supportedAreaCheckers.get(areaID.getAreaID()).test(shot));
        shot.setDatetime(new Date());
        shot.setOwnerLogin(login);
        shot.setProcessingTimeNano(System.nanoTime() - calculationsBeginTime);
        return shot;
    }

    /**
     * @throws IllegalArgumentException if checkArgsEnoughForHitCheck failed
     */
    public static boolean checkMironHit(@NonNull Shot shot) {
        checkArgsEnoughForHitCheck(shot);
        return checkMironHit(shot.getX(), shot.getY(), shot.getScope());
    }

    public static boolean checkMironHit(double x, double y, double scope) {
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

    /**
     * @throws IllegalArgumentException if checkArgsEnoughForHitCheck failed
     */
    public static boolean checkVadimHit(@NonNull Shot shot) {
        checkArgsEnoughForHitCheck(shot);
        return checkVadimHit(shot.getX(), shot.getY(), shot.getScope());
    }

    public static boolean checkVadimHit(double x, double y, double scope) {
        boolean isInLeftDown =
                x <= 0 && y <= 0;
        boolean isInRightDown =
                x >= 0 && y <= 0;
        boolean isInRightUP =
                x >= 0 && y >= 0;

        boolean isInRightUpCircle = isInRightUP &&
                x*x + y*y <= scope*scope;
        boolean isInLeftDownSquare = isInLeftDown &&
                (-1) * scope <= x &&
                (-1) * scope <= y/2;
        boolean inInRightDownTriangle = isInRightDown &&
                (1) * 0.5 * scope + 0.5 * y<=x;

        return isInRightUpCircle || isInLeftDownSquare || inInRightDownTriangle;
    }


    private static void checkArgsEnoughForHitCheck(@NonNull Shot shot) {
        if (shot.getX() == null || shot.getY() == null || shot.getScope() == null) {
            throw new IllegalArgumentException("null values on important for checkHit fields");
        }
    }
}
