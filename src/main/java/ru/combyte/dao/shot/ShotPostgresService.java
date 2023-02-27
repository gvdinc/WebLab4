package ru.combyte.dao.shot;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;

import java.util.List;
import java.util.Optional;

@Component
public class ShotPostgresService implements ShotDAO {
    private ShotPostgresDAO shotPostgresDAO;

    @Autowired
    public ShotPostgresService(ShotPostgresDAO shotPostgresDAO) {
        this.shotPostgresDAO = shotPostgresDAO;
    }

    @Override
    public void addShot(@NonNull Shot shot) {
        shotPostgresDAO.save(shot);
    }

    @Override
    public List<Shot> getShots(@NonNull AreaID areaID) {
        return shotPostgresDAO.findByAreaID(areaID.getAreaID());
    }
}
