package de.banana.flightviewer.controller;

import de.banana.flightviewer.model.Sample;
import de.banana.flightviewer.repository.SampleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for handling API requests related to flight data.
 * <p>
 * Provides endpoints to retrieve flight samples and track data for a given flight.
 * </p>
 * <ul>
 *     <li><b>GET /api/flights/{id}/samples</b>: Returns a list of flight samples for the specified flight.</li>
 *     <li><b>GET /api/flights/{id}/track</b>: Returns a GeoJSON LineString of the flight path.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/flights")
public class FlightApiController {

    private final SampleRepository samples;

    /**
     * Constructs a new FlightApiController with the given SampleRepository.
     *
     * @param samples the repository for accessing flight samples
     */
    public FlightApiController(SampleRepository samples) {
        this.samples = samples;
    }

    /**
     * Retrieves all samples for a given flight, ordered by timestamp.
     *
     * @param id the ID of the flight
     * @return a list of SampleDto objects representing the flight samples
     */
    @GetMapping("/{id}/samples")
    public List<SampleDto> samples(@PathVariable Long id){
        var list = samples.findByFlightIdOrderByTimestamp(id);
        if (!list.isEmpty()) {
            var s = list.get(0);
            System.out.println("[FlightApi] First sample: lat=" + s.getLatitude()
                    + ", lon=" + s.getLongitude()
                    + ", alt=" + s.getAltitude()
                    + ", ts=" + s.getTimestamp().toEpochMilli());
        }
        return list.stream().map(SampleDto::from).toList();
    }

    /**
     * Returns a GeoJSON LineString representing the flight path for the given flight ID.
     *
     * @param id the ID of the flight
     * @return a map containing the GeoJSON LineString
     */
    @GetMapping("/{id}/track")
    public Map<String, Object> track(@PathVariable Long id) {
        var coords = samples.findByFlightIdOrderByTimestamp(id)
                .stream()
                .map(s -> List.of(s.getLongitude(), s.getLatitude()))
                .toList();
        return Map.of("type", "LineString", "coordinates", coords);
    }

    /**
     * Data Transfer Object for exposing sample data via the API.
     */
    public static class SampleDto {
        public double latitude;
        public double longitude;
        public double altitude;
        public long timestamp;
        public double speed;
        public int battery;

        /**
         * Creates a SampleDto from a Sample entity.
         *
         * @param s the Sample entity
         * @return a SampleDto representing the sample
         */
        static SampleDto from(Sample s) {
            return new SampleDto(
                    s.getTimestamp().toEpochMilli(),
                    s.getLatitude(),
                    s.getLongitude(),
                    s.getAltitude(),
                    s.getSpeed(),
                    s.getBatteryPercent());
        }

        public SampleDto(long timestamp, double latitude, double longitude, double altitude, double speed, int battery) {
            this.timestamp = timestamp;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
            this.speed = speed;
            this.battery = battery;
        }
    }
}
