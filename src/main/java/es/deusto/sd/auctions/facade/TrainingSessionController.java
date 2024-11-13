package es.deusto.sd.auctions.facade;

import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.service.TrainingSessionService;

import java.time.LocalDate;
import java.util.List;


public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    // Endpoint para crear una nueva sesión
    public Sesion createSession( Sesion sesion) {
        return trainingSessionService.createSession(sesion);
    }

    // Endpoint para obtener las últimas 5 sesiones
    public List<Sesion> getRecentSessions() {
        return trainingSessionService.getRecentSessions();
    }

    public List<Sesion> getSessionsByDateRange(
             	String startDate,
             	String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return trainingSessionService.getSessionsByDateRange(start, end);
    }
}
