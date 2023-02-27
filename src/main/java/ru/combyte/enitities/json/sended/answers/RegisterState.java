package ru.combyte.enitities.json.sended.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterState {
    @JsonProperty("register_state")
    State state;

    public enum State {
        REGISTERED, DUPLICATE_LOGIN;
        @JsonValue
        public String toJsonValue() {
            return name().toLowerCase();
        }
    }
}