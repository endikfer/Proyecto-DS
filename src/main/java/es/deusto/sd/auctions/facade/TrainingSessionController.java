import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.service.TrainingSessionService;

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

    // Endpoint para crear una nueva sesión
    @PostMapping("/create")
    public Sesion createSession(@RequestBody Sesion sesion) {
        return trainingSessionService.createSession(sesion);
    }

    // Endpoint para obtener las últimas 5 sesiones
    @GetMapping("/recent")
    public List<Sesion> getRecentSessions() {
        return trainingSessionService.getRecentSessions();
    }

    // Endpoint para obtener sesiones en un rango de fechas
    @GetMapping("/range")
    public List<Sesion> getSessionsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return trainingSessionService.getSessionsByDateRange(start, end);
    }
}
