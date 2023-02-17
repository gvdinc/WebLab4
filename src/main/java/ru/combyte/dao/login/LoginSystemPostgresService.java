package ru.combyte.dao.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.combyte.enitities.User;

@Component
public class LoginSystemPostgresService implements LoginSystemDAO {
    private LoginSystemPostgresDAO loginSystemPostgresDAO;

    @Autowired
    public LoginSystemPostgresService(LoginSystemPostgresDAO loginSystemPostgresDAO) {
        this.loginSystemPostgresDAO = loginSystemPostgresDAO;
    }

    @Override
    public boolean isLoginPresented(String login) {
        return loginSystemPostgresDAO.findByLogin(login).isPresent();
    }

    @Override
    public boolean isUserPresented(String login, String passwordHash) {
        return loginSystemPostgresDAO.findByLoginAndPasswordHash(login, passwordHash).isPresent();
    }

    @Override
    public void register(String login, String passwordHash) {
        var user = new User();
        user.setLogin(login);
        user.setPasswordHash(passwordHash);
        loginSystemPostgresDAO.save(user);
    }
}
