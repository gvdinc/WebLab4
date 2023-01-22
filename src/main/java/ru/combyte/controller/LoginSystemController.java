package ru.combyte.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import ru.combyte.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class LoginSystemController {
    private final static List<String> LOGIN_REQUIRED_PARAMS;
    private final static List<String> REGISTER_REQUIRED_PARAMS;
    private final static List<String> SHOT_REQUIRED_PARAMS;

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

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, LOGIN_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) { // todo: add aop
            return getAbsentKeysAnswer(absentKeys);
        }

    }
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, REGISTER_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }

    }
    @PostMapping(value = "/shot", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> shot(@RequestParam Map<String, String> params) {
        var absentKeys = Utils.getNotPresentedKeysList(params, SHOT_REQUIRED_PARAMS);
        if (!absentKeys.isEmpty()) {
            return getAbsentKeysAnswer(absentKeys);
        }

    }

    private ResponseEntity<Object> getAbsentKeysAnswer(List<String> absentKeys) {
        var root = new JSONObject();
        root.append("absent_keys", new JSONArray(absentKeys));
        return new ResponseEntity<>(root, HttpStatus.BAD_REQUEST);
    }
}
