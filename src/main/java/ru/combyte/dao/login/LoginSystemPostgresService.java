package ru.combyte.dao.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.combyte.beans.User;

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
        return loginSystemPostgresDAO.findByLoginAndPassword(login, password).isPresent();
    }

    @Override
    public void register(String login, String password) {
        var user = new User();
        user.setLogin(login);
        user.setPassword(password);
        loginSystemPostgresDAO.save(user);
    }
}
