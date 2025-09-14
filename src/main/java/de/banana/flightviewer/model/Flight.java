package de.banana.flightviewer.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing a drone flight.
 * <p>
 * Stores metadata about a flight, including start/end times, distance, altitude, battery, and source file.
 * </p>
 */
@Entity
public class Flight {

    /**
     * Unique identifier for the flight (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Timestamp when the flight started.
     */
    private Instant startTime;
    /**
     * Timestamp when the flight ended.
     */
    private Instant endTime;
    /**
     * Total distance flown in meters.
     */
    private double distanceMeters;
    /**
     * Maximum altitude reached during the flight, in meters.
     */
    private double maxAltitudeMeters;
    /**
     * Minimum battery percentage recorded during the flight.
     */
    private int minBatteryPercent;
    /**
     * Name of the source file from which this flight was imported.
     */
    private String sourceFileName;

    /* ---------- getters & setters ---------- */

    /**
     * Gets the unique identifier for the flight.
     * @return the flight ID
     */
    public Long getId() { return id; }
    /**
     * Sets the unique identifier for the flight.
     * @param id the flight ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the timestamp when the flight started.
     * @return the start time
     */
    public Instant getStartTime() { return startTime; }
    /**
     * Sets the timestamp when the flight started.
     * @param startTime the start time
     */
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    /**
     * Gets the timestamp when the flight ended.
     * @return the end time
     */
    public Instant getEndTime() { return endTime; }
    /**
     * Sets the timestamp when the flight ended.
     * @param endTime the end time
     */
    public void setEndTime(Instant endTime) { this.endTime = endTime; }

    /**
     * Gets the total distance flown in meters.
     * @return the distance in meters
     */
    public double getDistanceMeters() { return distanceMeters; }
    /**
     * Sets the total distance flown in meters.
     * @param distanceMeters the distance in meters
     */
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }

    /**
     * Gets the maximum altitude reached during the flight, in meters.
     * @return the maximum altitude in meters
     */
    public double getMaxAltitudeMeters() { return maxAltitudeMeters; }
    /**
     * Sets the maximum altitude reached during the flight, in meters.
     * @param maxAltitudeMeters the maximum altitude in meters
     */
    public void setMaxAltitudeMeters(double maxAltitudeMeters) { this.maxAltitudeMeters = maxAltitudeMeters; }

    /**
     * Gets the minimum battery percentage recorded during the flight.
     * @return the minimum battery percentage
     */
    public int getMinBatteryPercent() { return minBatteryPercent; }
    /**
     * Sets the minimum battery percentage recorded during the flight.
     * @param minBatteryPercent the minimum battery percentage
     */
    public void setMinBatteryPercent(int minBatteryPercent) { this.minBatteryPercent = minBatteryPercent; }

    /**
     * Gets the name of the source file from which this flight was imported.
     * @return the source file name
     */
    public String getSourceFileName() { return sourceFileName; }
    /**
     * Sets the name of the source file from which this flight was imported.
     * @param sourceFileName the source file name
     */
    public void setSourceFileName(String sourceFileName) { this.sourceFileName = sourceFileName; }
}
