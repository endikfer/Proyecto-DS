package es.deusto.sd.auctions.facade;

import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.service.TrainingSessionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sesion")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {
        this.trainingSessionService = trainingSessionService;
    }

    // Crear una nueva sesión
    @PostMapping("/sesiones/crear")
    public SesionDTO crearSesion(@RequestParam("sesionId") Long id,
		    @RequestParam("titulo") String titulo,
		    @RequestParam("deporte") String deporte,
		    @RequestParam("distancia") double distancia,
		    @RequestParam("fechaInicio") LocalDate fechaInicio,
		    @RequestParam("tiempo") int duracion) {
    	SesionDTO sesionDTO = new SesionDTO(titulo, deporte, distancia, fechaInicio, duracion);
        return trainingSessionService.crearSesion(sesionDTO); // Llamamos al servicio para crear la sesión
    }

    // Obtener las últimas 5 sesiones
    @GetMapping("/sesiones/recientes")
    public List<SesionDTO> getSesionesRecientes() {
        return trainingSessionService.getSesionesRecientes(); // Llamamos al servicio para obtener las últimas 5 sesiones
    }

    // Obtener sesiones dentro de un rango de fechas
    @GetMapping("/sesiones/sesionesporfecha")
    public List<SesionDTO> getSesionesPorFecha(
            @RequestParam (value = "startDate") String startDate,
            @RequestParam (value = "endDate") String endDate) {
        return trainingSessionService.getSesionesPorFecha(startDate, endDate); // Llamamos al servicio para obtener sesiones en el rango de fechas
    }
}
