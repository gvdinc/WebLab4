package ru.combyte.dao.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.combyte.enitities.db.UserDb;
import ru.combyte.enitities.json.received.UserJson;

import java.util.Optional;

@Repository
public interface LoginSystemPostgresDAO extends JpaRepository<UserDb, Long> {
    Optional<UserDb> findByLogin(String login);
    Optional<UserDb> findByLoginAndPasswordHash(String login, String passwordHash);
}
