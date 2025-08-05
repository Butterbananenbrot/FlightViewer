package de.banana.flightviewer.controller;

import de.banana.flightviewer.repository.FlightRepository;
import de.banana.flightviewer.service.CsvImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FlightPageController {

    private final FlightRepository flights;
    private final CsvImportService importer;

    public FlightPageController(FlightRepository flights, CsvImportService importer) {
        this.flights = flights;
        this.importer = importer;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("flights", flights.findAll());
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file, RedirectAttributes ra) {
        try {
            importer.importCsv(file);
            ra.addFlashAttribute("msg", "Imported successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/flight/{id}")
    public String flight(@PathVariable Long id, Model model) {
        model.addAttribute("flightId", id);
        return "flight";
    }
}
