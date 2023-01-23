package ru.combyte.dao.shot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.combyte.beans.Shot;

@Repository
public interface ShotPostgresDAO extends JpaRepository<Shot, Long> {}