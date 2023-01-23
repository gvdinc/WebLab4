package ru.combyte.beans;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="shots")
public class Shot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="owner_login")
    private String ownerLogin;
    @Column(name = "x_coordinate")
    private double x;
    @Column(name = "y_coordinate")
    private double y;
    private double scope;
    private boolean hit;
    private Date datetime;
    @Column(name = "processing_time_nano")
    private long processingTimeNano;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public long getProcessingTimeNano() {
        return processingTimeNano;
    }

    public void setProcessingTimeNano(long processingTimeNano) {
        this.processingTimeNano = processingTimeNano;
    }
}
