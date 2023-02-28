package ru.combyte.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.combyte.controller.utils.LoginControllerUtils;
import ru.combyte.dao.login.LoginSystemDAO;
import ru.combyte.enitities.json.received.UserJson;
import ru.combyte.enitities.json.sended.answers.LoginState;
import ru.combyte.enitities.json.sended.answers.RegisterState;
import ru.combyte.enitities.json.sended.errors.ErrorType;

import java.util.Optional;

import static ru.combyte.controller.utils.LoginControllerUtils.getWrongCharactersValues;
import static ru.combyte.controller.utils.LoginControllerUtils.getWrongLengthValues;
import static ru.combyte.enitities.json.sended.answers.LoginState.State.*;
import static ru.combyte.enitities.json.sended.answers.RegisterState.State.DUPLICATE_LOGIN;
import static ru.combyte.enitities.json.sended.answers.RegisterState.State.REGISTERED;
import static ru.combyte.enitities.json.sended.errors.ErrorType.Type.ABSENT_KEY;

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
        var logonState = new LoginState(LOGON);
        return new ResponseEntity<>(logonState, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody UserJson user) {
        var registerFormatError = getRegisterFormatError(user);
        if (registerFormatError.isPresent()) return registerFormatError.get();
        if (loginSystemDAO.isLoginPresented(user.getLogin())) {
            var registerState = new RegisterState(DUPLICATE_LOGIN);
            return new ResponseEntity<>(registerState, HttpStatus.OK);
        }
        loginSystemDAO.register(user.getLogin(), user.getPassword());
        var registerState = new RegisterState(REGISTERED);
        return new ResponseEntity<>(registerState, HttpStatus.OK);
    }

    private Optional<ResponseEntity> getRegisterFormatError(@NonNull UserJson user) {
        var registerStructureMissingKeys = LoginControllerUtils.getCommandWithFullUserMissingKeys(user);
        if (!registerStructureMissingKeys.isEmpty()) {
            var errorType = new ErrorType(ABSENT_KEY, registerStructureMissingKeys);
            return Optional.of(new ResponseEntity<>(errorType, HttpStatus.UNPROCESSABLE_ENTITY));
        }
        var wrongLengthValues = getWrongLengthValues(user);
        if (!wrongLengthValues.isEmpty()) {
            var loginState = new LoginState(WRONG_LENGTH, wrongLengthValues);
            return Optional.of(new ResponseEntity<>(loginState, HttpStatus.OK));
        }
        var wrongCharactersValues = getWrongCharactersValues(user);
        if (!wrongCharactersValues.isEmpty()) {
            var loginState = new LoginState(WRONG_CHARACTER, wrongCharactersValues);
            return Optional.of(new ResponseEntity<>(loginState, HttpStatus.OK));
        }
        return Optional.empty();
    }
}
