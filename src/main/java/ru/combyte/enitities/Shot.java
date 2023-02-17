package ru.combyte.enitities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.combyte.Utils;

import java.util.Date;

@Entity
@Table(name="shots")
// lombok
@Getter
@Setter
public class Shot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(name="owner_login")
    @JsonIgnore
    private String ownerLogin;
    @Column(name = "x_coordinate")
    @JsonProperty("x")
    private Double x;
    @Column(name = "y_coordinate")
    @JsonProperty("y")
    private Double y;
    @Column(name = "scope")
    @JsonProperty("R")
    private Double scope;
    @Column(name = "hit")
    @JsonProperty("hit")
    private Boolean hit;
    @Column(name = "datetime")
    private Date datetime;
    @Column(name = "processing_time_nano")
    @JsonProperty("processing_time_nano")
    private Long processingTimeNano;

    @JsonProperty("datetime")
    public String getDateAsISO8601() {
        return Utils.getDateAsISO8601(datetime);
    }
}
