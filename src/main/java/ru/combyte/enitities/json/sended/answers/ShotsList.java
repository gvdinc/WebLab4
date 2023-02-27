package ru.combyte.enitities.json.sended.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.combyte.enitities.Shot;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ShotsList {
    @JsonProperty("shots")
    List<Shot> shots;
}
