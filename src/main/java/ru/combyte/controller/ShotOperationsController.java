package ru.combyte.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ru.combyte.area.AreaChecker;
import ru.combyte.controller.utils.ShotControllerUtils;
import ru.combyte.dao.shot.ShotDAO;
import ru.combyte.enitities.Shot;
import ru.combyte.enitities.json.received.AreaID;
import ru.combyte.enitities.json.received.UserJson;
import ru.combyte.enitities.json.sended.answers.ShotAnswer;
import ru.combyte.enitities.json.sended.answers.ShotsList;
import ru.combyte.enitities.json.sended.errors.ErrorType;

import static ru.combyte.controller.utils.ShotControllerUtils.getWrongValueValues;
import static ru.combyte.enitities.json.sended.errors.ErrorType.Type.ABSENT_KEY;

@RestController
@SessionAttributes("loginSessionState")
public class ShotOperationsController {
    private ShotDAO shotDAO;
    private ObjectMapper objectMapper;

    @Autowired
    public ShotOperationsController(ShotDAO shotDAO, ObjectMapper objectMapper) {
        this.shotDAO = shotDAO;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/shot",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public ResponseEntity<Object> shot(HttpServletRequest request) {
        var cachedHttpRequest = (ContentCachingRequestWrapper) request;
        var shot = objectMapper.readValue(cachedHttpRequest.getContentAsByteArray(), Shot.class);
        var shotCommandStructureMissingKeys = ShotControllerUtils.getCommandWithClientShotMissingKeys(shot);
        if (!shotCommandStructureMissingKeys.isEmpty()) {
            var errorType = new ErrorType(ABSENT_KEY, shotCommandStructureMissingKeys);
            return new ResponseEntity<>(errorType, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var wrongValueValues = getWrongValueValues(shot);
        if (!wrongValueValues.isEmpty()) {
            var shotAnswer = ShotAnswer.initWrongValueValuesAnswer(wrongValueValues);
            return new ResponseEntity<>(shotAnswer, HttpStatus.OK);
        }
        shot.setOwnerLogin(objectMapper.readValue(cachedHttpRequest.getContentAsByteArray(), UserJson.class).getLogin());
        shot = AreaChecker.shot(new AreaID(shot.getAreaID()), shot.getX(), shot.getY(), shot.getScope(), shot.getOwnerLogin());
        shotDAO.addShot(shot);
        return new ResponseEntity<>(new ShotAnswer(shot), HttpStatus.OK);
    }

    @PostMapping(value = "/shots",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public ResponseEntity<Object> shots(HttpServletRequest request) {
        var cachedHttpRequest = (ContentCachingRequestWrapper) request;
        var areaID = objectMapper.readValue(cachedHttpRequest.getContentAsByteArray(), AreaID.class);
        var areaIDMissingKeys = ShotControllerUtils.getCommandWithAreaIDMissingKeys(areaID);
        if (areaID.getAreaID() == null) {
            var errorType = new ErrorType(ABSENT_KEY, areaIDMissingKeys);
            return new ResponseEntity<>(errorType, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        var shots = shotDAO.getShots(areaID);
        return new ResponseEntity<>(new ShotsList(shots), HttpStatus.OK);
    }
}
