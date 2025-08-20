package de.banana.flightviewer.controller;

import de.banana.flightviewer.model.Sample;
import de.banana.flightviewer.repository.SampleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for handling API requests related to flight data.
 * Provides endpoints to retrieve flight samples and track data.
 */
@RestController
@RequestMapping("/api/flights")
public class FlightApiController {

    private final SampleRepository samples;

    public FlightApiController(SampleRepository samples) {
        this.samples = samples;
    }

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


    @GetMapping("/{id}/track")
    public Map<String, Object> track(@PathVariable Long id) {
        var coords = samples.findByFlightIdOrderByTimestamp(id)
                .stream()
                .map(s -> List.of(s.getLongitude(), s.getLatitude()))
                .toList();
        return Map.of("type", "LineString", "coordinates", coords);
    }

    /* DTO */
    public record SampleDto(long ts, double latitude, double longitude,
                            double altitude, double speed, int battery) {
        static SampleDto from(Sample s) {
            return new SampleDto(
                    s.getTimestamp().toEpochMilli(),
                    s.getLatitude(),
                    s.getLongitude(),
                    s.getAltitude(),
                    s.getSpeed(),
                    s.getBatteryPercent());
        }
    }
}
