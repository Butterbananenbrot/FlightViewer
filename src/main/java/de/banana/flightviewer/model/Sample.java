package de.banana.flightviewer.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Entity representing a single data sample (telemetry point) from a drone flight.
 * <p>
 * Each sample is associated with a flight and contains timestamp, position, altitude, speed, and battery data.
 * </p>
 */
@Entity
public class Sample {

    /**
     * Unique identifier for the sample (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The flight to which this sample belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Flight flight;

    /**
     * Timestamp of the sample.
     */
    private Instant timestamp;
    /**
     * Latitude in decimal degrees.
     */
    private double latitude;
    /**
     * Longitude in decimal degrees.
     */
    private double longitude;
    /**
     * Altitude in meters above ground level.
     */
    private double altitude;
    /**
     * Horizontal speed in meters per second.
     */
    private double speed;
    /**
     * Battery percentage at the time of the sample.
     */
    private int batteryPercent;

    /* ---------- getters & setters ---------- */

    /**
     * Gets the unique identifier of the sample.
     * @return the sample ID
     */
    public Long getId() { return id; }
    /**
     * Sets the unique identifier of the sample.
     * @param id the sample ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the flight associated with this sample.
     * @return the flight
     */
    public Flight getFlight() { return flight; }
    /**
     * Sets the flight associated with this sample.
     * @param flight the flight
     */
    public void setFlight(Flight flight) { this.flight = flight; }

    /**
     * Gets the timestamp of the sample.
     * @return the timestamp
     */
    public Instant getTimestamp() { return timestamp; }
    /**
     * Sets the timestamp of the sample.
     * @param timestamp the timestamp
     */
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    /**
     * Gets the latitude of the sample.
     * @return the latitude
     */
    public double getLatitude() { return latitude; }
    /**
     * Sets the latitude of the sample.
     * @param latitude the latitude
     */
    public void setLatitude(double latitude) { this.latitude = latitude; }

    /**
     * Gets the longitude of the sample.
     * @return the longitude
     */
    public double getLongitude() { return longitude; }
    /**
     * Sets the longitude of the sample.
     * @param longitude the longitude
     */
    public void setLongitude(double longitude) { this.longitude = longitude; }

    /**
     * Gets the altitude of the sample.
     * @return the altitude
     */
    public double getAltitude() { return altitude; }
    /**
     * Sets the altitude of the sample.
     * @param altitude the altitude
     */
    public void setAltitude(double altitude) { this.altitude = altitude; }

    /**
     * Gets the speed of the sample.
     * @return the speed
     */
    public double getSpeed() { return speed; }
    /**
     * Sets the speed of the sample.
     * @param speed the speed
     */
    public void setSpeed(double speed) { this.speed = speed; }

    /**
     * Gets the battery percentage of the sample.
     * @return the battery percentage
     */
    public int getBatteryPercent() { return batteryPercent; }
    /**
     * Sets the battery percentage of the sample.
     * @param batteryPercent the battery percentage
     */
    public void setBatteryPercent(int batteryPercent) { this.batteryPercent = batteryPercent; }
}
