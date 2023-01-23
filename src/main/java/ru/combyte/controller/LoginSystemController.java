package ru.combyte.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.combyte.Utils;
import ru.combyte.area.AreaChecker;
import ru.combyte.beans.LoginSessionState;
import ru.combyte.beans.Shot;
import ru.combyte.dao.login.LoginSystemDAO;
import ru.combyte.dao.shot.ShotDAO;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@SessionAttributes("loginSessionState")
public class LoginSystemController {
    private final static List<String> LOGIN_REQUIRED_PARAMS;
    private final static List<String> REGISTER_REQUIRED_PARAMS;
    private final static List<String> SHOT_REQUIRED_PARAMS;
    private final static int MIN_LOGIN_LENGTH = 5;
    private final static int MAX_LOGIN_LENGTH = 20;
    private final static int PASSWORD_HASH_LENGTH = 8;
    private final static Pattern LOGIN_CHARS_PATTERN_CHECK =
            Pattern.compile("^[a-zA-Z0-9]{%s,%s}$".formatted(MIN_LOGIN_LENGTH, MAX_LOGIN_LENGTH));

    static {
        LOGIN_REQUIRED_PARAMS = Arrays.asList(new String[] {
                "login",
                "password_hash"
        });
        REGISTER_REQUIRED_PARAMS = Arrays.asList(new String[] {
                "login",
                "password_hash"
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

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody String sentJSONAsString,
                                        @ModelAttribute("loginSessionState") LoginSessionState loginSessionState) {
        JSONObject requestParams;
        try {
             requestParams = new JSONObject(sentJSONAsString);
        } catch (JSONException e) {
            return getWrongJSONStructureAnswer();
        }
        var absentKeys = Utils.getNotPresentedKeysList(requestParams.toMap(), LOGIN_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }
        String login;
        String passwordHash;
        try {
            login = requestParams.getString("login");
            passwordHash = requestParams.getString("password_hash");
        } catch (JSONException e) {
            return getWrongJSONStructureAnswer();
        }
        if (!loginSystemDAO.isLoginPresented(login)) {
            return new ResponseEntity<>(LoginState.WRONG_LOGIN.asJSON().toString(), HttpStatus.OK);
        }
        if (loginSystemDAO.isUserPresented(login, passwordHash)) {
            loginSessionState.setLogin(login);
            loginSessionState.setLogon(true);
            return new ResponseEntity<>(LoginState.LOGON.asJSON().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(LoginState.WRONG_PASSWORD.asJSON().toString(), HttpStatus.OK);
        }
    }

    @ModelAttribute("loginSessionState")
    public LoginSessionState getLoginSessionState() {
        return new LoginSessionState();
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> logout(@ModelAttribute("loginSessionState") LoginSessionState loginSessionState) {
        if (!loginSessionState.isLogon()) {
            var root = new JSONObject();
            root.put("not_entered", true);
            return new ResponseEntity<>(root.toString(), HttpStatus.OK);
        }
        loginSessionState.setLogon(false);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestBody String sentJSONAsString) {
        JSONObject requestParams;
        try {
            requestParams = new JSONObject(sentJSONAsString);
        } catch (JSONException e) {
            return getWrongJSONStructureAnswer();
        }
        var absentKeys = Utils.getNotPresentedKeysList(requestParams.toMap(), REGISTER_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }
        String login;
        String passwordHash;
        try {
            login = requestParams.getString("login");
            passwordHash = requestParams.getString("password_hash");
        } catch (JSONException e) {
            return getWrongJSONStructureAnswer();
        }
        if (loginSystemDAO.isLoginPresented(login)) {
            var duplicateLoginAnswerRoot = RegisterState.DUPLICATE_LOGIN.asJSON();
            return new ResponseEntity<>(duplicateLoginAnswerRoot.toString(), HttpStatus.OK);
        }
        var boundErrorValues = getBoundErrorRegisterValues(login, passwordHash);
        if (!boundErrorValues.isEmpty()) {
            var wrongParamsLengthAnswerRoot = getRegisterUserParamsWrongLengthJSONAnswer(boundErrorValues);
            return new ResponseEntity<>(wrongParamsLengthAnswerRoot.toString(), HttpStatus.OK);
        }
        if (!LOGIN_CHARS_PATTERN_CHECK.matcher(login).matches()) {
            var wrongLoginCharsAnswerRoot = getRegisterLoginWrongPatternJSONAnswer();
            return new ResponseEntity<>(wrongLoginCharsAnswerRoot.toString(), HttpStatus.OK);
        }
        loginSystemDAO.register(login, passwordHash);
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

    private List<String> getBoundErrorRegisterValues(String login, String passwordHash) {
        var boundErrorValues = new LinkedList<String>();
        if (login.length() < MIN_LOGIN_LENGTH || MAX_LOGIN_LENGTH < login.length()) {
            boundErrorValues.add("login");
        }
        if (passwordHash.length() != PASSWORD_HASH_LENGTH) {
            boundErrorValues.add("password_hash");
        }
        return boundErrorValues;
    }

    @PostMapping(value = "/shot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shot(@RequestBody String sentJSONAsString,
                                       @ModelAttribute("loginSessionState") LoginSessionState loginSessionState) {
        if (!loginSessionState.isLogon()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        JSONObject requestParams;
        try {
            requestParams = new JSONObject(sentJSONAsString);
        } catch (JSONException e) {
            return getWrongJSONStructureAnswer();
        }
        var absentKeys = Utils.getNotPresentedKeysList(requestParams.toMap(), SHOT_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }
        double x = -1;
        double y = -1;
        double r = -1;
        List<String> badTypeParams = new LinkedList<>();
        try{
            x = requestParams.getDouble("x");
        } catch (JSONException e) {
            badTypeParams.add("x");
        }
        try {
            y = requestParams.getDouble("y");
        } catch (JSONException e) {
            badTypeParams.add("y");
        }
        try {
            r = requestParams.getDouble("R");
        } catch (JSONException e) {
            badTypeParams.add("R");
        }
        if (!badTypeParams.isEmpty()) {
            var root = new JSONObject();
            root.put("wrong_type", new JSONArray(badTypeParams));
            return new ResponseEntity<>(root.toString(), HttpStatus.OK);
        }
        var shot = AreaChecker.shot(x, y, r, loginSessionState.getLogin());
        shotDAO.addShot(shot);
        var answerRoot = new JSONObject();
        answerRoot.put("hit", shot.isHit());
        answerRoot.put("datetime", shot.getDatetime().toInstant().toString());
        answerRoot.put("processing_time_nano", shot.getProcessingTimeNano());
        return new ResponseEntity<>(answerRoot.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/shots", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shots(@ModelAttribute("loginSessionState") LoginSessionState loginSessionState) {
        if (!loginSessionState.isLogon()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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

    private ResponseEntity<Object> getWrongJSONStructureAnswer() {
        var root = new JSONObject();
        root.put("error_type", "wrong_json_structure");
        return new ResponseEntity<>(root.toString(), HttpStatus.BAD_REQUEST);
    }
}
