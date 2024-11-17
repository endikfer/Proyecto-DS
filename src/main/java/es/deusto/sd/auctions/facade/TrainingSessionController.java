package es.deusto.sd.auctions.facade;

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

    // Crear una nueva sesión
    @PostMapping("/create")
    public SesionDTO createSession(@RequestBody SesionDTO sesionDTO) {
        return trainingSessionService.createSession(sesionDTO); // Llamamos al servicio para crear la sesión
    }

    // Obtener las últimas 5 sesiones
    @GetMapping("/recent")
    public List<SesionDTO> getRecentSessions() {
        return trainingSessionService.getRecentSessions(); // Llamamos al servicio para obtener las últimas 5 sesiones
    }

    // Obtener sesiones dentro de un rango de fechas
    @GetMapping("/range")
    public List<SesionDTO> getSessionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return trainingSessionService.getSessionsByDateRange(startDate, endDate); // Llamamos al servicio para obtener sesiones en el rango de fechas
    }
}
