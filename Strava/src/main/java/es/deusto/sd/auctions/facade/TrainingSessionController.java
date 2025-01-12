package es.deusto.sd.auctions.facade;

import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.dto.UsuarioDTO;
import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sesion")
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
	@PostMapping("/sesiones/crear")
	public ResponseEntity<Void> crearSesion(@RequestParam("sesionId") Long id, @RequestParam("titulo") String titulo,
			@RequestParam("deporte") String deporte, @RequestParam("distancia") double distancia,
			@RequestParam("fechaInicio") LocalDate fechaInicio, @RequestParam("tiempo") int duracion) {
		try {
			SesionDTO sesionDTO = new SesionDTO(titulo, deporte, distancia, fechaInicio, duracion);
			trainingSessionService.crearSesion(sesionDTO);

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
	@GetMapping("/sesiones/recientes")
	public ResponseEntity<List<Sesion>> getSesionesRecientes() {
		try {
			List<SesionDTO> sesionesDTO = trainingSessionService.getSesionesRecientes();

			List<Sesion> sesiones = sesionesDTO.stream()
					.map(dto -> new Sesion(null, null, dto.getTitulo(), Deporte.valueOf(dto.getDeporte().toUpperCase()),
							dto.getDistancia(), dto.getFechaInicio(), dto.getDuracion()))
					.toList();
			if (sesiones.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			// Retornamos la lista de sesiones con estado OK
			return new ResponseEntity<>(sesiones, HttpStatus.OK);
		} catch (Exception e) {
			// En caso de error, retornamos INTERNAL_SERVER_ERROR
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(summary = "Obtener sesiones entre fechas", description = "Devuelve sesiones entre un rango de fechas", responses = {
			@ApiResponse(responseCode = "200", description = "Sesiones obtenidas correctamente"),
			@ApiResponse(responseCode = "204", description = "No se encontraron sesiones en el rango de fechas"),
			@ApiResponse(responseCode = "400", description = "Formato de fecha inválido"),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor") })
	@GetMapping("/sesiones/sesionesporfecha")
	public ResponseEntity<List<Sesion>> getSesionesPorFecha(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate) {
		try {
			LocalDate start = LocalDate.parse(startDate);
			LocalDate end = LocalDate.parse(endDate);

			List<SesionDTO> sesionesDTO = trainingSessionService.getSesionesPorFecha(startDate, endDate);

			List<Sesion> sesiones = sesionesDTO.stream()
					.map(dto -> new Sesion(null, null, dto.getTitulo(), Deporte.valueOf(dto.getDeporte().toUpperCase()),
							dto.getDistancia(), dto.getFechaInicio(), dto.getDuracion()))
					.toList();
			if (sesiones.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			// Retornar sesiones con estado OK
			return new ResponseEntity<>(sesiones, HttpStatus.OK);
		} catch (DateTimeParseException e) {
			// Manejar formato de fecha inválido
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// Manejar errores generales
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
