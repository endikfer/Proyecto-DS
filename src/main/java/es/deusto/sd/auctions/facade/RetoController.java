package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import es.deusto.sd.auctions.dto.RetoAcptadoDTO;
import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.entity.Reto;
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
		    @RequestParam("retoId") Long id,
		    @RequestParam("nombre") String nombre,
		    @RequestParam("deporte") String deporte,
		    @RequestParam("fecha_inicio") LocalDate fecha_inicio,
		    @RequestParam("fecha_fin") LocalDate fecha_fin,
		    @RequestParam("objetivo") String objetivo,
		    @RequestParam("distancia") Integer distancia,
		    @RequestParam("tiempo") Integer tiempo,
		    @RequestHeader(value = "Authorization") String token
		){
		    try {
		    	Usuario user = usuarioService.getUserByToken(token);
		    	
		    	if (user == null) {
		    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    	}
		    	
		        retoService.crearReto(id, nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo);
		        
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
		    @RequestHeader(value = "Authorization") String token) {
		    
		    try {
		        // Verifica si el usuario está autorizado
		        Usuario user = usuarioService.getUserByToken(token);
		        if (user == null) {
		            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		        }

		        // Obtiene la lista de retos
		        Collection<Reto> retos = retoService.obtenerRetos();

		        // Convierte la fecha desde el parámetro si se pasa, sino usa la fecha actual
		        LocalDate fechaBusqueda = (fechaFiltroStr != null && !fechaFiltroStr.isEmpty()) ? LocalDate.parse(fechaFiltroStr) : LocalDate.now();

		        // Filtra los retos por fecha
		        List<Reto> retosFiltradosPorFecha = new ArrayList<>();
		        for (Reto reto : retos) {
		            if (reto.getFecha_fin().isAfter(fechaBusqueda)) {
		                retosFiltradosPorFecha.add(reto);
		            }
		        }

		        // Filtra los retos por deporte si se proporciona
		        List<Reto> retosFiltradosPorDeporte = new ArrayList<>();
		        if (deporteFiltro != null && !deporteFiltro.isEmpty()) {
		            for (Reto reto : retosFiltradosPorFecha) {
		                if (reto.getDeporte().name().equalsIgnoreCase(deporteFiltro)) {
		                    retosFiltradosPorDeporte.add(reto);
		                }
		            }
		        }

		        // Si no se filtra por deporte, mantén los retos filtrados solo por fecha
		        List<Reto> resultadosFinales = (deporteFiltro != null && !deporteFiltro.isEmpty()) ? retosFiltradosPorDeporte : retosFiltradosPorFecha;

		        // Si no hay resultados, devuelve un 204 No Content
		        if (resultadosFinales.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }

		        // Ordena los resultados por la fecha de inicio
		        List<Reto> retosOrdenados = new ArrayList<>(resultadosFinales);
		        retosOrdenados.sort((r1, r2) -> r2.getFecha_inicio().compareTo(r1.getFecha_inicio()));

		        // Devuelve los últimos 5 retos
		        List<RetoDTO> dtos = new ArrayList<>();
		        for (Reto reto : retosOrdenados) {
		            dtos.add(retoToDTO(reto));
		        }

		        return new ResponseEntity<>(dtos, HttpStatus.OK);
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
			        @RequestHeader(value = "Authorization") String token) {

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
			        @RequestHeader(value = "Authorization") String token) {

			    try {
			        Usuario usuario = usuarioService.getUserByToken(token);
			        if (usuario == null) {
			            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			        }
			        System.out.println("Entrada");
			        List<RetoAcptadoDTO> retosAceptados = retoService.getRetosAceptados(usuario.getId());
			        System.out.println("1");
			        if (retosAceptados.isEmpty()) {
			            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			        }
			        return new ResponseEntity<>(retosAceptados, HttpStatus.OK);
			    } catch (Exception e) {
			        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			    }
			}
		
		private RetoDTO retoToDTO(Reto reto) {
			return new RetoDTO( reto.getId(), 
					reto.getNombre(),
					reto.getDeporte().name().toLowerCase(),
					reto.getFecha_inicio(),
					reto.getFecha_fin(),
					reto.getDistancia(), 
					reto.getTiempo());
		}
}
