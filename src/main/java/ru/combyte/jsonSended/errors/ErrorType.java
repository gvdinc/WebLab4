package ru.combyte.jsonSended.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public class ErrorType {
    @JsonProperty("error_type")
    Type type;
    @JsonProperty("absent_keys")
    List<String> absentKeys;

    /**
     * @throws IllegalArgumentException if type is ABSENT_KEY - should have absentKeys list, use another constructor
     * @throws NullPointerException if type is null
     */
    public ErrorType(@NonNull Type type) {
        this(type, null);
    }

    /**
     * @param absentKeys null if type is not ABSENT_KEY
     * @throws IllegalArgumentException if absentKeys is not null when not ABSENT_KEY or if null or empty when it is
     * @throws NullPointerException if type is null
     */
    public ErrorType(@NonNull Type type, List<String> absentKeys) {
        switch (type) {
            case ABSENT_KEY -> {
                if (absentKeys == null || absentKeys.isEmpty()) {
                    throw new IllegalArgumentException("Error type is absent key, so the list with these keys cannot be null or empty");
                }
            }
            default -> {
                if (absentKeys != null) {
                    throw new IllegalArgumentException("Error type is not absent key, so the list with absent keys should be null");
                }
            }
        }
        this.type = type;
        switch (type) {
            case ABSENT_KEY -> {
                this.absentKeys = absentKeys;
            }
        }
    }

    public enum Type {
        WRONG_JSON_STRUCTURE, ABSENT_KEY;

        @JsonValue
        public String toJsonValue() {
            return name().toLowerCase();
        }
    }
}
