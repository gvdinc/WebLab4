package ru.combyte.dao.shot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.combyte.enitities.Shot;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShotPostgresDAO extends JpaRepository<Shot, Long> {
    List<Shot> findByAreaID(String areaID);
}
