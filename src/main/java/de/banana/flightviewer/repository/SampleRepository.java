package de.banana.flightviewer.repository;

import de.banana.flightviewer.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findByFlightIdOrderByTimestamp(Long flightId);
}
