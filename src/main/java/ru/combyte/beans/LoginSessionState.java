package ru.combyte.beans;

public class LoginSessionState {
    private String login;
    private boolean logon = false;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isLogon() {
        return logon;
    }

    public void setLogon(boolean logon) {
        this.logon = logon;
    }
}
