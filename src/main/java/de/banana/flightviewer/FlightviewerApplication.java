package de.banana.flightviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the FlightViewer Spring Boot application.
 * <p>
 * This application provides a web interface for visualizing DJI drone flight logs in CSV format.
 * It uses Spring Boot for backend services and Thymeleaf for rendering web pages.
 * </p>
 */
@SpringBootApplication
public class FlightviewerApplication {

    /**
     * Starts the FlightViewer application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        SpringApplication.run(FlightviewerApplication.class, args);
    }
}
