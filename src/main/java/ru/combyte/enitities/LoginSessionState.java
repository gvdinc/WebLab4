package ru.combyte.enitities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSessionState {
    private String login;
    private boolean logon = false;
}
