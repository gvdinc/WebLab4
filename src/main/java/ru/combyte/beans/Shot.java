package ru.combyte.beans;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Shot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "owner_id")
    private Long owner_id;
    @Column(name = "x_coordinate")
    private double x;
    @Column(name = "y_coordinate")
    private double y;
    @Column(name = "scope")
    private double scope;
    @Column(name = "hit")
    private boolean hit;
    @Column(name = "datetime")
    private Date datetime;
    @Column(name = "processing_time_nano")
    private long processingTimeNano;
}
