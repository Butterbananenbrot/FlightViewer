// This service will handle the CSV import logic.
// It will read CSV files, parse the data, and save it to the database.

package de.banana.flightviewer.service;

import de.banana.flightviewer.model.Flight;
import de.banana.flightviewer.model.Sample;
import de.banana.flightviewer.repository.FlightRepository;
import de.banana.flightviewer.repository.SampleRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvImportService {

    private final FlightRepository flightRepo;
    private final SampleRepository sampleRepo;

    public CsvImportService(FlightRepository flightRepo, SampleRepository sampleRepo) {
        this.flightRepo = flightRepo;
        this.sampleRepo = sampleRepo;
    }

    // main entry point for processing a CSV file
    public Flight importCsv(MultipartFile file) throws IOException {
        List<Sample> samples = new ArrayList<>();
        Instant start = null, end = null;
        double maxAlt = Double.NEGATIVE_INFINITY;
        int minBatt = 100;
        double distance = 0.0;
        Sample prev = null;

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord r : parser) {
                Instant ts = Instant.ofEpochMilli(Long.parseLong(r.get("time(ms)")));
                double lat = Double.parseDouble(r.get("latitude"));
                double lon = Double.parseDouble(r.get("longitude"));
                double alt = Double.parseDouble(r.get("height(m)"));
                double spd = Double.parseDouble(r.get("velocity(m/s)"));
                int batt = Integer.parseInt(r.get("battery(%)"));

                Sample s = new Sample();
                s.setTimestamp(ts);
                s.setLatitude(lat);
                s.setLongitude(lon);
                s.setAltitude(alt);
                s.setSpeed(spd);
                s.setBatteryPercent(batt);

                samples.add(s);

                if (start == null) start = ts;
                end = ts;
                if (alt > maxAlt) maxAlt = alt;
                if (batt < minBatt) minBatt = batt;
                if (prev != null) distance += haversine(prev.getLatitude(), prev.getLongitude(), lat, lon);
                prev = s;
            }
        }

        Flight f = new Flight();
        f.setStartTime(start);
        f.setEndTime(end);
        f.setMaxAltitudeMeters(maxAlt);
        f.setMinBatteryPercent(minBatt);
        f.setDistanceMeters(distance);
        f.setSourceFileName(file.getOriginalFilename());
        flightRepo.save(f);

        for (Sample s : samples) s.setFlight(f);
        sampleRepo.saveAll(samples);

        return f;
    }

    //The haversine method computes the great-circle distance between two geographic points
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // metres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }
}
