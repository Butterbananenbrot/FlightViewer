package de.banana.flightviewer.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startTime;
    private Instant endTime;
    private double distanceMeters;
    private double maxAltitudeMeters;
    private int minBatteryPercent;
    private String sourceFileName;

    /* ---------- getters & setters ---------- */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }

    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

    public double getMaxAltitudeMeters() { return maxAltitudeMeters; }
    public void setMaxAltitudeMeters(double maxAltitudeMeters) { this.maxAltitudeMeters = maxAltitudeMeters; }

    public int getMinBatteryPercent() { return minBatteryPercent; }
    public void setMinBatteryPercent(int minBatteryPercent) { this.minBatteryPercent = minBatteryPercent; }

    public String getSourceFileName() { return sourceFileName; }
    public void setSourceFileName(String sourceFileName) { this.sourceFileName = sourceFileName; }
}

