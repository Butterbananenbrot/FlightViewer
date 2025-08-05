package de.banana.flightviewer.repository;

import de.banana.flightviewer.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {}
// This interface extends JpaRepository to provide CRUD operations for the Flight entity.