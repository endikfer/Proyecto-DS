package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import es.deusto.sd.auctions.dto.RetoAcptadoDTO;
import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/acciones")
@Tag(name = "Retos Controller", description = "Operaciones relacionadas con los retos")
public class RetoController {
	
	private final UsuarioService usuarioService;
	private final RetoService retoService;

	public RetoController(UsuarioService usuarioService, RetoService retoService) {
		this.usuarioService = usuarioService;
		this.retoService = retoService;
	}
	
	
	// Crear Reto
		@Operation(
			    summary = "Crear reto",
			    description = "Crea un reto",
			    responses = {
			        @ApiResponse(responseCode = "200", description = "OK: Reto creado correctamente"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: Usuario no autenticado"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
		@PostMapping("/retos/crear")
		public ResponseEntity<List<RetoDTO>> crearReto(
		    @RequestParam("nombre") String nombre,
		    @RequestParam("deporte") String deporte,
		    @RequestParam("fecha_inicio") LocalDate fecha_inicio,
		    @RequestParam("fecha_fin") LocalDate fecha_fin,
		    @RequestParam("distancia") Integer distancia,
		    @RequestParam("tiempo") Integer tiempo,
		    @RequestParam(value = "Authorization") String token
		){
		    try {
		    	Usuario user = usuarioService.getUserByToken(token);
		    	
		    	if (user == null) {
		    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    	}
		    	
		        retoService.crearReto(nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo);
		        
		        return new ResponseEntity<>(HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
		
		@Operation(
			    summary = "Consultar los últimos retos aceptados",
			    description = "Devuelve los últimos 5 retos aceptados por el usuario en formato DTO",
			    responses = {
			        @ApiResponse(responseCode = "200", description = "OK: Lista de los últimos 5 retos devueltos correctamente"),
			        @ApiResponse(responseCode = "204", description = "No Content: No hay retos aceptados"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: Usuario no autenticado"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
		@GetMapping("/retos")
		public ResponseEntity<List<RetoDTO>> consultarReto(
		    @Parameter(name = "deporte", description = "Deporte", required = false, example = "running")
		    @RequestParam(value = "deporte", required = false) String deporteFiltro,

		    @Parameter(name = "fecha", description = "Fecha", required = false, example = "2024-11-16")
		    @RequestParam(value = "fecha", required = false) String fechaFiltroStr,

		    @Parameter(name = "token", description = "Authorization token", required = true, example = "1727786726773")
		    @RequestParam(value = "Authorization") String token) {
		    
		    try {
		        // Verifica si el usuario está autorizado
		        Usuario user = usuarioService.getUserByToken(token);
		        if (user == null) {
		            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		        }
		        
		        List<RetoDTO> retos = retoService.obtenerRetos(deporteFiltro, fechaFiltroStr);

		        return new ResponseEntity<>(retos, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		
		
		@Operation(
			    summary = "Aceptar un reto",
			    description = "Permite a un usuario aceptar un reto existente",
			    responses = {
			        @ApiResponse(responseCode = "200", description = "OK: Reto aceptado correctamente"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: Usuario no autenticado"),
			        @ApiResponse(responseCode = "409", description = "Conflict: El reto ya fue aceptado previamente"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
			@PostMapping("/retos/aceptar")
			public ResponseEntity<Void> aceptarReto(
			        @RequestParam("retoId") Long retoId,
			        @Parameter(name = "token", description = "Authorization token", required = true)
			        @RequestParam(value = "Authorization") String token) {

			    try {
			        Usuario usuario = usuarioService.getUserByToken(token);
			        if (usuario == null) {
			            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			        }

			        boolean retoAceptado = retoService.aceptarReto(retoId, usuario.getId());
			        if (!retoAceptado) {
			            return new ResponseEntity<>(HttpStatus.CONFLICT);
			        }

			        return new ResponseEntity<>(HttpStatus.OK);
			    } catch (Exception e) {
			        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			    }
			}
		
		
		@Operation(
			    summary = "Consultar retos aceptados",
			    description = "Devuelve los retos aceptados por el usuario",
			    responses = {
			        @ApiResponse(responseCode = "200", description = "OK: Lista de retos aceptados devuelta correctamente"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: Usuario no autenticado"),
			        @ApiResponse(responseCode = "204", description = "No Content: No hay retos aceptados"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
		@GetMapping("/retos/aceptados")
		public ResponseEntity<List<RetoAcptadoDTO>> consultarRetosAceptados(
		        @Parameter(name = "token", description = "Authorization token", required = true)
		        @RequestParam(value = "Authorization") String token) {

		    try {
		        Usuario usuario = usuarioService.getUserByToken(token);
		        if (usuario == null) {
		            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		        }
		        List<RetoAcptadoDTO> retosAceptados = retoService.getRetosAceptados(usuario.getId());
		        if (retosAceptados.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }
		        return new ResponseEntity<>(retosAceptados, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}
}
