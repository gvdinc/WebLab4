package ru.combyte.dao.shot;

import ru.combyte.enitities.Shot;

import java.util.List;

public interface ShotDAO {
    void addShot(Shot shot);
    List<Shot> getShots();
}
