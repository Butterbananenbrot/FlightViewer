package de.banana.flightviewer.repository;

import de.banana.flightviewer.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for accessing and managing Sample entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations and custom query methods for Sample.
 * </p>
 */
public interface SampleRepository extends JpaRepository<Sample, Long> {
    /**
     * Finds all samples for a given flight, ordered by timestamp.
     *
     * @param flightId the ID of the flight
     * @return a list of samples ordered by timestamp
     */
    List<Sample> findByFlightIdOrderByTimestamp(Long flightId);
}
