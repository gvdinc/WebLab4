package ru.combyte.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.combyte.controller.utils.LoginControllerUtils;
import ru.combyte.dao.login.LoginSystemDAO;
import ru.combyte.enitities.User;
import ru.combyte.jsonSended.answers.LoginState;
import ru.combyte.jsonSended.answers.RegisterState;
import ru.combyte.jsonSended.errors.ErrorType;

import static ru.combyte.controller.utils.LoginControllerUtils.getWrongCharactersValues;
import static ru.combyte.controller.utils.LoginControllerUtils.getWrongLengthValues;
import static ru.combyte.jsonSended.answers.LoginState.State.*;
import static ru.combyte.jsonSended.answers.RegisterState.State.DUPLICATE_LOGIN;
import static ru.combyte.jsonSended.answers.RegisterState.State.REGISTERED;
import static ru.combyte.jsonSended.errors.ErrorType.Type.ABSENT_KEY;

@RestController
@SessionAttributes("loginSessionState")
public class LoginSystemController {
    private LoginSystemDAO loginSystemDAO;

    public ObjectMapper objectMapper;

    @Autowired
    public LoginSystemController(LoginSystemDAO loginSystemDAO, ObjectMapper objectMapper) {
        this.loginSystemDAO = loginSystemDAO;
        this.objectMapper = objectMapper;
    }


    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login() throws JsonProcessingException {
        return new ResponseEntity<>(new LoginState(LOGON), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody User user) {
        var registerStructureMissingKeys = LoginControllerUtils.getCommandWithFullUserMissingKeys(user);
        if (!registerStructureMissingKeys.isEmpty()) {
            var errorType = new ErrorType(ABSENT_KEY, registerStructureMissingKeys);
            return new ResponseEntity<>(errorType, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var wrongLengthValues = getWrongLengthValues(user);
        if (!wrongLengthValues.isEmpty()) {
            var loginState = new LoginState(WRONG_LENGTH, wrongLengthValues);
            return new ResponseEntity<>(loginState, HttpStatus.OK);
        }
        var wrongCharactersValues = getWrongCharactersValues(user);
        if (!wrongCharactersValues.isEmpty()) {
            var loginState = new LoginState(WRONG_CHARACTER, wrongCharactersValues);
            return new ResponseEntity<>(loginState, HttpStatus.OK);
        }
        if (loginSystemDAO.isLoginPresented(user.getLogin())) {
            var registerState = new RegisterState(DUPLICATE_LOGIN);
            return new ResponseEntity<>(registerState, HttpStatus.OK);
        }
        loginSystemDAO.register(user.getLogin(), user.getPasswordHash());
        var registerState = new RegisterState(REGISTERED);
        return new ResponseEntity<>(registerState, HttpStatus.OK);
    }
}
