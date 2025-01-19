package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.SesionDTO;
import es.deusto.sd.strava.entity.Deporte;
import es.deusto.sd.strava.entity.Sesion;
import es.deusto.sd.strava.service.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class TrainingSessionController {

	private final TrainingSessionService trainingSessionService;

	public TrainingSessionController(TrainingSessionService trainingSessionService) {
		this.trainingSessionService = trainingSessionService;
	}

	@Operation(summary = "Crear sesion", description = "Crea un sesion", responses = {
			@ApiResponse(responseCode = "200", description = "OK: sesion creado correctamente"),
			@ApiResponse(responseCode = "400", description = "Bad Request: Datos inválidos o incompletos"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	// Crear una nueva sesión
	@PostMapping("/crear")
	public ResponseEntity<Void> crearSesion(
			@RequestParam("titulo") String titulo,
			@RequestParam("deporte") String deporte, 
			@RequestParam("distancia") double distancia,
			@RequestParam("fechaInicio") String fechaInicio, 
			@RequestParam("duracion") int duracion) {
		try {
// Definir el formato de la fecha
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

// Convertir fechaInicio de String a LocalDate
			LocalDate fechaInicioParsed = LocalDate.parse(fechaInicio, formatter);

// Crear la sesión con la fecha convertida
			Sesion sesion = new Sesion(titulo, Deporte.valueOf(deporte.toUpperCase()), distancia, fechaInicioParsed,
					duracion);

// Llamar al servicio para guardar la sesión
			trainingSessionService.crearSesion(sesion);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Error de solicitud mal formada
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Obtener sesiones recientes", description = "Devuelve las ultimas 5 sesiones", responses = {
			@ApiResponse(responseCode = "200", description = "OK: Lista de sesiones devuelta exitosamente"),
			@ApiResponse(responseCode = "204", description = "No Content: No existen sesiones"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	// Obtener las últimas 5 sesiones
	@GetMapping("/recientes")
	public ResponseEntity<List<SesionDTO>> getSesionesRecientes() {
		try {
			List<Sesion> sesiones = trainingSessionService.getSesionesRecientes();
			if (sesiones.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<SesionDTO> sesionesDTO = new ArrayList<SesionDTO>();
			sesiones.forEach(sesion -> sesionesDTO.add(toDTO(sesion)));
			return new ResponseEntity<>(sesionesDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Obtener sesiones entre fechas", description = "Devuelve sesiones entre un rango de fechas", responses = {
			@ApiResponse(responseCode = "200", description = "Sesiones obtenidas correctamente"),
			@ApiResponse(responseCode = "204", description = "No se encontraron sesiones en el rango de fechas"),
			@ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping("/sesionesporfecha")
	public ResponseEntity<List<SesionDTO>> getSesionesPorFecha(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate) {
		try {

			List<SesionDTO> sesionesDTO = trainingSessionService.getSesionesPorFecha(startDate, endDate);

			if (sesionesDTO.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			// Retornar sesiones con estado OK
			return new ResponseEntity<>(sesionesDTO, HttpStatus.OK);
		} catch (DateTimeParseException e) {
			// Manejar formato de fecha inválido
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// Manejar errores generales
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Método para convertir Sesion a SesionDTO
	private SesionDTO toDTO(Sesion sesion) {
		// Formato para las fechas
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return new SesionDTO(sesion.getId(), sesion.getTitulo(), sesion.getDeporte().name().toLowerCase(), // Convertir
																											// Enum a
																											// minúsculas
				sesion.getDistancia(), sesion.getFechaInicio().format(formatter), // Convertir LocalDate a String
				sesion.getDuracion());
	}

}
