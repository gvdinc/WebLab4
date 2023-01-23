package ru.combyte.dao.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.combyte.beans.User;

import java.util.Optional;

@Repository
public interface LoginSystemPostgresDAO extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPasswordHash(String login, String passwordHash);
}
