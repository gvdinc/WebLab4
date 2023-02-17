package ru.combyte.dao.shot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.combyte.enitities.Shot;

import java.util.List;

@Component
public class ShotPostgresService implements ShotDAO {
    private ShotPostgresDAO shotPostgresDAO;

    @Autowired
    public ShotPostgresService(ShotPostgresDAO shotPostgresDAO) {
        this.shotPostgresDAO = shotPostgresDAO;
    }

    @Override
    public void addShot(Shot shot) {
        shotPostgresDAO.save(shot);
    }

    @Override
    public List<Shot> getShots() {
        return shotPostgresDAO.findAll();
    }
}
