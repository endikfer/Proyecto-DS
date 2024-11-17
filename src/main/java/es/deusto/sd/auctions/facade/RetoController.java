package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public class RetoController {
	
	private final UsuarioService usuarioService;
	private final RetoService retoService;

	public RetoController(UsuarioService usuarioService, RetoService retoService) {
		this.usuarioService = usuarioService;
		this.retoService = retoService;
	}
	
	
	// Crear Reto
		@Operation(
			    summary = "Create reto",
			    description = "Returns a list of all available retos",
			    responses = {
			        @ApiResponse(responseCode = "200", description = "OK: List of retos retrieved successfully"),
			        @ApiResponse(responseCode = "204", description = "No Content: No retos found"),
			        @ApiResponse(responseCode = "400", description = "Bad Request: Invalid objective specified"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: User not authenticated"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
		@GetMapping("/retos")
		public ResponseEntity<List<RetoDTO>> crearReto(
				@Parameter(name = "id", description = "Identificador del reto", required = true, example = "01")		
				@PathVariable("id") Long id,
				@Parameter(name = "nombre", description = "Nombre del reto a crear", required = true, example = "50 flexiones")		
				@PathVariable("nombre") String nombre,
				@Parameter(name = "deporte", description = "Nombre del deporte", required = true, example = "running")
				@RequestParam("deporte") String deporte,
				@Parameter(name = "fecha_inicio", description = "Fecha de inicio del reto", required = true, example = "2024-11-16")
				@RequestParam("fecha_inicio") LocalDate fecha_inicio,
				@Parameter(name = "fecha_fin", description = "Fecha de finalizacion del reto", required = true, example = "2024-11-22")
				@RequestParam("fecha_fin") LocalDate fecha_fin,
				@Parameter(name = "objetivo", description = "Objetivo a realizar en el reto", required = true, example = "distancia")
				@RequestParam("objetivo") String objetivo,
				@Parameter(name = "distancia", description = "Distancia a realizar", required = false, example = "200")
				@RequestParam("distancia") Integer distancia,
				@Parameter(name = "tiempo", description = "Tiempo a completar", required = false, example = "31")
				@RequestParam("tiempo") Integer tiempo,
				@Parameter(name = "token", description = "Token de autorizacion", required = true, example = "1727786726773")
				@RequestBody String token) { 
		    try {
		    	Usuario user = usuarioService.getUserByToken(token);
		    	
		    	if (user == null) {
		    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    	}
		    	
		        retoService.crearReto(id, nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo);
		        
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
			        @ApiResponse(responseCode = "400", description = "Bad Request: Formato de fecha incorrecto"),
			        @ApiResponse(responseCode = "401", description = "Unauthorized: Usuario no autenticado"),
			        @ApiResponse(responseCode = "500", description = "Internal server error")
			    }
			)
		@GetMapping("/retos")
		public ResponseEntity<List<RetoDTO>> consultarReto(
				@Parameter(name = "deporte", description = "Deporte", required = false, example = "2024-11-16")
				@RequestParam("deporte") String deporteFiltro,
				@Parameter(name = "fecha", description = "Fecha", required = false, example = "2024-11-16")
				@RequestParam("fecha") LocalDate fechaFiltro,
				@Parameter(name = "token", description = "Authorization token", required = true, example = "1727786726773")
	    		@RequestBody String token) {
		    

		    try {
		    	Usuario user = usuarioService.getUserByToken(token);
		    	
		    	if (user == null) {
		    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    	}
		    	
		    	Collection<Reto> retos = retoService.obtenerRetos();
		    	
		    	LocalDate fechaBusqueda = (fechaFiltro != null) ? fechaFiltro : LocalDate.now();

		    	List<Reto> retosFiltradosPorFecha = new ArrayList<>();
		    	List<Reto> retosFiltradosPorDeporte = new ArrayList<>();

		    	for (Reto reto : retos) {
		    	    if (reto.getFecha_fin().isAfter(fechaBusqueda)) {
		    	        retosFiltradosPorFecha.add(reto);
		    	    }
		    	}

		    	if (deporteFiltro != null && !deporteFiltro.isEmpty()) {
		    	    for (Reto reto : retos) {
		    	        if (reto.getDeporte().equalsIgnoreCase(deporteFiltro)) {
		    	            retosFiltradosPorDeporte.add(reto);
		    	        }
		    	    }
		    	}

		    	List<Reto> resultadosFinales;
		    	if (deporteFiltro != null && !deporteFiltro.isEmpty()) {
		    	    resultadosFinales = retosFiltradosPorDeporte;
		    	} else {
		    	    resultadosFinales = retosFiltradosPorFecha;
		    	}

		        if (resultadosFinales.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }

		        List<Reto> retosOrdenados = new ArrayList<>(resultadosFinales);

			    retosOrdenados.sort((r1, r2) -> r2.getFecha_inicio().compareTo(r1.getFecha_inicio()));
	
			    List<Reto> ultimos5Retos = new ArrayList<>();
			    for (int i = 0; i < retosOrdenados.size() && i < 5; i++) {
			        ultimos5Retos.add(retosOrdenados.get(i));
			    }
		        
		        List<RetoDTO> dtos = new ArrayList<>();
		        for (Reto reto : ultimos5Retos) {
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
			@PostMapping("/retos")
			public ResponseEntity<Void> aceptarReto(
			        @Parameter(name = "retoId", description = "ID del reto a aceptar", required = true)
			        @PathVariable Long retoId,
			        @Parameter(name = "token", description = "Authorization token", required = true)
			        @RequestHeader("token") String token) {

			    try {
			        Usuario usuario = usuarioService.getUserByToken(token);
			        if (usuario == null) {
			            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			        }

			        boolean retoAceptado = retoService.aceptarReto(retoId, usuario);
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
			@GetMapping("/retos")
			public ResponseEntity<List<RetoDTO>> consultarRetosAceptados(
			        @Parameter(name = "token", description = "Authorization token", required = true)
			        @RequestHeader("token") String token) {

			    try {
			        Usuario usuario = usuarioService.getUserByToken(token);
			        if (usuario == null) {
			            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			        }

			        List<Reto> retosAceptados = retoService.getRetosAceptados(usuario);
			        if (retosAceptados.isEmpty()) {
			            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			        }

			        List<RetoDTO> dtos = new ArrayList<RetoDTO>();
			        for(Reto r:retosAceptados) {
			        	dtos.add(retoToDTO(r));
			        }
			        return new ResponseEntity<>(dtos, HttpStatus.OK);
			    } catch (Exception e) {
			        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			    }
			}


		
		
		
		private RetoDTO retoToDTO(Reto reto) {
			return new RetoDTO( reto.getId(), 
					reto.getNombre(),
					reto.getDeporte(),
					reto.getFecha_inicio(),
					reto.getFecha_fin(),
					reto.getDistancia(), 
					reto.getTiempo());
		}
}
