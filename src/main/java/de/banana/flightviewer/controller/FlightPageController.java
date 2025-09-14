package de.banana.flightviewer.controller;

import de.banana.flightviewer.repository.FlightRepository;
import de.banana.flightviewer.service.CsvImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling web page requests related to flights.
 * <p>
 * Provides endpoints for:
 * <ul>
 *     <li>Displaying all flights on the index page (GET /)</li>
 *     <li>Uploading flight data via CSV (POST /upload)</li>
 *     <li>Displaying a specific flight's details (GET /flight/{id})</li>
 * </ul>
 * </p>
 */
@Controller
public class FlightPageController {

    /**
     * Repository for accessing flight data.
     */
    private final FlightRepository flights;
    /**
     * Service for importing flight data from CSV files.
     */
    private final CsvImportService importer;

    /**
     * Constructs a new FlightPageController with the given dependencies.
     *
     * @param flights  the flight repository
     * @param importer the CSV import service
     */
    public FlightPageController(FlightRepository flights, CsvImportService importer) {
        this.flights = flights;
        this.importer = importer;
    }

    /**
     * Handles requests to the index page, displaying all flights.
     *
     * @param model the model to add attributes to for the view
     * @return the name of the index view template
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("flights", flights.findAll());
        return "index";
    }

    /**
     * Handles uploading a CSV file and importing flight data.
     *
     * @param file the uploaded CSV file
     * @param redirectAttributes attributes for flash messages
     * @return redirect to the index page
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            importer.importCsv(file);
            redirectAttributes.addFlashAttribute("msg", "Imported successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    /**
     * Handles requests to display a specific flight's details.
     *
     * @param id the ID of the flight
     * @param model the model to add attributes to for the view
     * @return the name of the flight view template
     */
    @GetMapping("/flight/{id}")
    public String flight(@PathVariable Long id, Model model) {
        model.addAttribute("flightId", id);
        return "flight";
    }
}
