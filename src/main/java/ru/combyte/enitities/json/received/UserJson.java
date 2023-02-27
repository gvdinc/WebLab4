package ru.combyte.enitities.json.received;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserJson {
    @JsonProperty("login")
    private String login;
    @JsonProperty("password")
    private String password;
}