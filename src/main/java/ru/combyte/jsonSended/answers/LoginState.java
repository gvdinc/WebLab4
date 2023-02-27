package ru.combyte.jsonSended.answers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.combyte.controller.LoginFilter;

import java.util.List;

@Getter
public class LoginState {
    @JsonProperty("login_state")
    State state;
    @JsonProperty("wrong_character_param")
    List<String> wrongCharacterParams;
    @JsonProperty("wrong_length_param")
    List<String> wrongLengthParams;

    /**
     * @throws IllegalArgumentException if state is WRONG_CHARACTER or WRONG_LENGTH - should have wrongParams list, use another constructor
     */
    public LoginState(@NonNull State state) {
        this(state, null);
    }

    /**
     * @param wrongParams null if state is not WRONG_CHARACTER or WRONG_LENGTH
     * @throws IllegalArgumentException if wrongParams is not null when not WRONG_CHARACTER or WRONG_LENGTH or if null or empty when it is
     */
    public LoginState(@NonNull State state, List<String> wrongParams) {
        switch (state) {
            case WRONG_CHARACTER, WRONG_LENGTH -> {
                if (wrongParams == null || wrongParams.isEmpty()) {
                    throw new IllegalArgumentException("Wrong character or length should be set with list of wrong params");
                }
            }
            default -> {
                if (wrongParams != null) {
                    throw new IllegalArgumentException("wrongParams is not null but not needed");
                }
            }
        }
        this.state = state;
        switch (state) {
            case WRONG_CHARACTER -> {
                this.wrongCharacterParams = wrongParams;
            }
            case WRONG_LENGTH -> {
                this.wrongLengthParams = wrongParams;
            }
        }
    }
    public enum State {
        LOGON, WRONG_LOGIN, WRONG_PASSWORD, WRONG_CHARACTER, WRONG_LENGTH;

        @JsonValue
        public String toJsonValue() {
            return name().toLowerCase();
        }
    }
}
