package ru.combyte.dao.shot;

import ru.combyte.beans.Shot;

import java.util.List;

public interface ShotDAO {
    void addShot(Shot shot);
    List<Shot> getShots();
}
