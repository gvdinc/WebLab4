package ru.combyte.dao.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.combyte.enitities.db.UserDb;
import ru.combyte.enitities.json.received.UserJson;

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
    public boolean isUserPresented(String login, String password) {
        String passwordHash = UserDb.getPasswordHash(password);
        return loginSystemPostgresDAO.findByLoginAndPasswordHash(login, passwordHash).isPresent();
    }

    @Override
    public void register(String login, String password) {
        var user = new UserDb();
        user.setLogin(login);
        user.setPasswordHash(UserDb.getPasswordHash(password));
        loginSystemPostgresDAO.save(user);
    }
}
