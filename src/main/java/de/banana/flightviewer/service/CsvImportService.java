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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for importing drone flight data from CSV files.
 * <p>
 * Imports a DJI FlightRecord *.TXT that has been converted to CSV by PhantomHelp, AirData, or DJI Assistant.
 * <br>
 * Required column names (exact, case-sensitive):
 * <ul>
 *   <li>OSD.flyTime [s]        (double seconds since motors-on)</li>
 *   <li>OSD.latitude           (decimal degrees)</li>
 *   <li>OSD.longitude          (decimal degrees)</li>
 *   <li>OSD.altitude [ft]      (double feet AGL)</li>
 *   <li>OSD.hSpeed [MPH]       (horizontal speed mph)</li>
 *   <li>BATTERY.chargeLevel    (int 0-100)</li>
 * </ul>
 * Any other columns are ignored.
 * </p>
 */
@Service
public class CsvImportService {

    /**
     * Repository for storing Flight entities.
     */
    private final FlightRepository flightRepo;
    /**
     * Repository for storing Sample entities.
     */
    private final SampleRepository sampleRepo;

    /**
     * Constructs a CsvImportService with the required repositories.
     *
     * @param flightRepo repository for Flight entities
     * @param sampleRepo repository for Sample entities
     */
    public CsvImportService(FlightRepository flightRepo, SampleRepository sampleRepo) {
        this.flightRepo = flightRepo;
        this.sampleRepo = sampleRepo;
    }

    /**
     * Imports flight data from a CSV file and stores it in the database.
     *
     * @param file the uploaded CSV file
     * @return the imported Flight entity
     * @throws IOException if an I/O error occurs during import
     */
    public Flight importCsv(MultipartFile file) throws IOException {

        List<Sample> samples = new ArrayList<>();

        // Stats we compute on-the-fly
        double maxAlt = Double.NEGATIVE_INFINITY;
        int    minBatt = 100;
        double distanceMeters = 0.0;

        Instant baseInstant = Instant.now(); // relative timestamps anchor here
        double  firstFlyTimeS = -1;          // will hold flyTime at first row

        Sample prev = null;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            /* Skip optional first line like "sep=," produced by some converters */
            br.mark(50);
            String maybeSep = br.readLine();
            if (maybeSep == null) {
                throw new IllegalArgumentException("Empty CSV");
            }
            if (!maybeSep.startsWith("sep=")) {
                br.reset();        // not a sep line → rewind
            }

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(br);

            for (CSVRecord r : parser) {
                double flySec = parseDoubleSafe(r, "OSD.flyTime [s]");
                if (firstFlyTimeS < 0) firstFlyTimeS = flySec;
                long   tsMillis = (long) ((flySec - firstFlyTimeS) * 1000.0);
                Instant ts = baseInstant.plusMillis(tsMillis);

                double lat  = parseDoubleSafe(r, "OSD.latitude");
                double lon  = parseDoubleSafe(r, "OSD.longitude");

                double altFt = parseDoubleSafe(r, "OSD.altitude [ft]");
                double altM  = altFt * 0.3048;                 // ft → m

                double spdMph = parseDoubleSafe(r, "OSD.hSpeed [MPH]");
                double spdMs  = spdMph * 0.44704;              // mph → m/s

                int batt = parseIntSafe(r, "BATTERY.chargeLevel");

                Sample s = new Sample();
                s.setTimestamp(ts);
                s.setLatitude(lat);
                s.setLongitude(lon);
                s.setAltitude(altM);
                s.setSpeed(spdMs);
                s.setBatteryPercent(batt);
                samples.add(s);

                if (altM > maxAlt) maxAlt = altM;
                if (batt < minBatt) minBatt = batt;
                if (prev != null) {
                    distanceMeters += haversine(prev.getLatitude(), prev.getLongitude(), lat, lon);
                }
                prev = s;
            }
        }

        /* Persist Flight summary */
        Flight flight = new Flight();
        flight.setStartTime(samples.isEmpty() ? baseInstant : samples.get(0).getTimestamp());
        flight.setEndTime(samples.isEmpty() ? baseInstant : samples.get(samples.size()-1).getTimestamp());
        flight.setMaxAltitudeMeters(maxAlt);
        flight.setMinBatteryPercent(minBatt);
        flight.setDistanceMeters(distanceMeters);
        flight.setSourceFileName(file.getOriginalFilename());
        flightRepo.save(flight);

        /* Link & save samples */
        for (Sample s : samples) s.setFlight(flight);
        sampleRepo.saveAll(samples);

        return flight;
    }

    /* ---------- Helpers ---------- */

    private static double parseDoubleSafe(CSVRecord r, String column) {
        String val = r.get(column);
        return val == null || val.isBlank() ? 0.0 : Double.parseDouble(val);
    }

    private static int parseIntSafe(CSVRecord r, String column) {
        String val = r.get(column);
        return val == null || val.isBlank() ? 0 : Integer.parseInt(val);
    }

    /** Great-circle distance in metres (Haversine). */
    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // earth radius (m)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }
}
