package ru.combyte.dao.login;

public interface LoginSystemDAO {
    boolean isLoginPresented(String login);
    boolean isUserPresented(String login, String passwordHash);
    void register(String login, String passwordHash);
}
