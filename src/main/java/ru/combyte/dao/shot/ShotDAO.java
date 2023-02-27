package ru.combyte.dao.shot;

import lombok.NonNull;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;

import java.util.List;
import java.util.Optional;

public interface ShotDAO {
    void addShot(@NonNull Shot shot);
    List<Shot> getShots(@NonNull AreaID areaID);
}
