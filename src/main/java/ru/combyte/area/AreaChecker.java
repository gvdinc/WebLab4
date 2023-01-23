package ru.combyte.area;

import ru.combyte.beans.Shot;

import java.util.Date;

public class AreaChecker {
    public static Shot shot(double x, double y, double scope) {
        var shot = new Shot();
        shot.setX(x);
        shot.setY(y);
        shot.setScope(scope);
        shot.setHit(true);
        shot.setDatetime(new Date());
        shot.setOwnerLogin("imperator");
        return shot;
    }
}
