package es.deusto.sd.auctions.controller;

import es.deusto.sd.auctions.dto.CrearSesionDTO;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.service.TrainingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    @Autowired
    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    @PostMapping("/create")
    public SesionDTO createSession(@RequestBody CrearSesionDTO createSesionDTO) {
        return trainingSessionService.createSession(createSesionDTO);
    }

    @GetMapping("/recent")
    public List<SesionDTO> getRecentSessions() {
        return trainingSessionService.getRecentSessions();
    }

    @GetMapping("/range")
    public List<SesionDTO> getSessionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return trainingSessionService.getSessionsByDateRange(startDate, endDate);
    }
}
