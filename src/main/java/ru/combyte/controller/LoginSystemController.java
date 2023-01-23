package ru.combyte.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.combyte.Utils;
import ru.combyte.area.AreaChecker;
import ru.combyte.beans.Shot;
import ru.combyte.dao.LoginSystemDAO;
import ru.combyte.dao.ShotDAO;

import java.util.*;
import java.util.regex.Pattern;

@RestController
public class LoginSystemController {
    private final static List<String> LOGIN_REQUIRED_PARAMS;
    private final static List<String> REGISTER_REQUIRED_PARAMS;
    private final static List<String> SHOT_REQUIRED_PARAMS;
    private final static int MIN_LOGIN_LENGTH = 5;
    private final static int MAX_LOGIN_LENGTH = 20;
    private final static int MIN_PASSWORD_LENGTH = 8;
    private final static int MAX_PASSWORD_LENGTH = 30;
    private final static Pattern LOGIN_CHARS_PATTERN_CHECK =
            Pattern.compile("^[a-zA-Z0-9]{%s,%s}$".formatted(MIN_LOGIN_LENGTH, MAX_LOGIN_LENGTH));

    static {
        LOGIN_REQUIRED_PARAMS = Arrays.asList(new String[] {
                "login",
                "password"
        });
        REGISTER_REQUIRED_PARAMS = Arrays.asList(new String[] {
                "login",
                "password"
        });
        SHOT_REQUIRED_PARAMS = Arrays.asList(new String[] {
                "x",
                "y",
                "R"
        });
    }

    private LoginSystemDAO loginSystemDAO;
    private ShotDAO shotDAO;


    @Autowired
    public LoginSystemController(LoginSystemDAO loginSystemDAO, ShotDAO shotDAO) {
        this.loginSystemDAO = loginSystemDAO;
        this.shotDAO = shotDAO;
    }

    private enum LoginState {
        LOGON, WRONG_LOGIN, WRONG_PASSWORD;

        public JSONObject asJSON() {
            var loginStateValue = switch (this) {
                case LOGON -> "logon";
                case WRONG_LOGIN -> "wrong_login";
                case WRONG_PASSWORD -> "wrong_password";
            };
            var root = new JSONObject();
            root.put("login_state", loginStateValue);
            return root;
        }
    }

    private enum RegisterState {
        REGISTERED, DUPLICATE_LOGIN, BAD_CONTENT;

        public JSONObject asJSON() {
            var registerStateValue = switch (this) {
                case REGISTERED -> "registered";
                case DUPLICATE_LOGIN -> "duplicate_login";
                case BAD_CONTENT -> "bad_content";
            };
            var root = new JSONObject();
            root.put("register_state", registerStateValue);
            return root;
        }
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, LOGIN_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) { // todo: add aop
            return getAbsentKeysAnswer(absentKeys);
        }
        var login = params.get("login");
        var password = params.get("password");
        if (!loginSystemDAO.isLoginPresented(login)) {
            return new ResponseEntity<>(LoginState.WRONG_LOGIN.asJSON().toString(), HttpStatus.OK);
        }
        if (loginSystemDAO.isUserPresented(login, password)) {
            return new ResponseEntity<>(LoginState.LOGON.asJSON().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(LoginState.WRONG_PASSWORD.asJSON().toString(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logout() {
        return new ResponseEntity<>(new Object(), HttpStatus.BAD_REQUEST); // todo:
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, REGISTER_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }
        var login = params.get("login");
        var password = params.get("password");
        if (loginSystemDAO.isLoginPresented(login)) {
            var duplicateLoginAnswerRoot = RegisterState.DUPLICATE_LOGIN.asJSON();
            return new ResponseEntity<>(duplicateLoginAnswerRoot.toString(), HttpStatus.OK);
        }
        var boundErrorValues = getBoundErrorRegisterValues(login, password);
        if (!boundErrorValues.isEmpty()) {
            var wrongParamsLengthAnswerRoot = getRegisterUserParamsWrongLengthJSONAnswer(boundErrorValues);
            return new ResponseEntity<>(wrongParamsLengthAnswerRoot.toString(), HttpStatus.OK);
        }
        if (!LOGIN_CHARS_PATTERN_CHECK.matcher(login).matches()) {
            var wrongLoginCharsAnswerRoot = getRegisterLoginWrongPatternJSONAnswer();
            return new ResponseEntity<>(wrongLoginCharsAnswerRoot.toString(), HttpStatus.OK);
        }
        loginSystemDAO.register(login, password);
        var registeredAnswerRoot = RegisterState.REGISTERED.asJSON();
        return new ResponseEntity<>(registeredAnswerRoot.toString(), HttpStatus.OK);
    }

    private JSONObject getRegisterLoginWrongPatternJSONAnswer() {
        var root = new JSONObject();
        Utils.appendProps(root, RegisterState.BAD_CONTENT.asJSON());
        root.put("wrong_login_character", true);
        return root;
    }

    private JSONObject getRegisterUserParamsWrongLengthJSONAnswer(List<String> wrongLengthParams) {
        var root = new JSONObject();
        Utils.appendProps(root, RegisterState.BAD_CONTENT.asJSON());
        root.put("wrong_length_params", new JSONArray(wrongLengthParams));
        return root;
    }

    private List<String> getBoundErrorRegisterValues(String login, String password) {
        var boundErrorValues = new LinkedList<String>();
        if (login.length() < MIN_LOGIN_LENGTH || MAX_LOGIN_LENGTH < login.length()) {
            boundErrorValues.add("login");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || MAX_PASSWORD_LENGTH < password.length()) {
            boundErrorValues.add("password");
        }
        return boundErrorValues;
    }

    @PostMapping(value = "/shot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shot(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, SHOT_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }
        // todo: user login check
        var xString = params.get("x");
        var yString = params.get("y");
        var rString = params.get("R");
        Map<String, String> nameToValue = new HashMap<>();
        nameToValue.put("x", xString);
        nameToValue.put("y", yString);
        nameToValue.put("R", rString);
        List<String> badTypeParams = getBadTypeShotParams(nameToValue);
        if (!badTypeParams.isEmpty()) {
            var root = new JSONObject();
            root.put("wrong_type", new JSONArray(badTypeParams));
            return new ResponseEntity<>(root.toString(), HttpStatus.OK);
        }
        var x = Double.parseDouble(xString);
        var y = Double.parseDouble(yString);
        var r = Double.parseDouble(rString);
        var shot = AreaChecker.shot(x, y, r);
        shotDAO.addShot(shot);
        var answerRoot = new JSONObject();
        answerRoot.put("hit", shot.isHit());
        answerRoot.put("datetime", shot.getDatetime().toInstant().toString());
        answerRoot.put("processing_time_nano", shot.getProcessingTimeNano());
        return new ResponseEntity<>(answerRoot.toString(), HttpStatus.OK);
    }

    private List<String> getBadTypeShotParams(Map<String, String> nameToValueMap) {
        List<String> badTypeShotParams = new LinkedList<>();
        for (Map.Entry<String, String> nameToValueEntry : nameToValueMap.entrySet()) {
            try {
                Double.parseDouble(nameToValueEntry.getValue());
            } catch (NumberFormatException e) {
                badTypeShotParams.add(nameToValueEntry.getKey());
            }
        }
        return badTypeShotParams;
    }

    @PostMapping(value = "/shots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shots() {
        var shots = shotDAO.getShots();
        var root = new JSONObject();
        root.put("shots", shotsToJSONArray(shots));
        return new ResponseEntity<>(root.toString(), HttpStatus.OK);
    }

    private JSONArray shotsToJSONArray(List<Shot> shots) {
        var shotsArray = new JSONArray(new LinkedList<>());
        for (var shot : shots) {
            var shotJSON = new JSONObject();
            shotJSON.put("x", shot.getX());
            shotJSON.put("y", shot.getY());
            shotJSON.put("R", shot.getScope());
            shotJSON.put("hit", shot.isHit());
            shotJSON.put("datetime", shot.getDatetime().toInstant().toString());
            shotJSON.put("processing_time_nano", shot.getProcessingTimeNano());
            shotsArray.put(shotJSON);
        }
        return shotsArray;
    }


    private ResponseEntity<Object> getAbsentKeysAnswer(List<String> absentKeys) {
        var root = new JSONObject();
        root.put("error_type", "absent_key");
        root.put("absent_keys", new JSONArray(absentKeys));
        return new ResponseEntity<>(root.toString(), HttpStatus.BAD_REQUEST);
    }
}
