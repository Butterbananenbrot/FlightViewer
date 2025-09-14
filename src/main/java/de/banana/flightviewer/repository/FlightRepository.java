package de.banana.flightviewer.repository;

import de.banana.flightviewer.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing Flight entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations and query methods for Flight.
 * </p>
 */
public interface FlightRepository extends JpaRepository<Flight, Long> {}

