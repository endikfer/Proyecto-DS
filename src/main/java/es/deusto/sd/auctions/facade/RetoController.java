package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.AuctionsService;
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
	
	public void aceptarReto(Reto reto, List<Sesion> sesiones) {
        // Verificar que las fechas de las sesiones son compatibles con las fechas del reto
        for (Sesion sesion : sesiones) {
            if (sesion.getFechaInicio().isBefore(reto.getFecha_inicio()) || sesion.getFechaInicio().isAfter(reto.getFecha_fin())) {
                throw new IllegalArgumentException("La sesión no se encuentra dentro del rango de fechas del reto.");
            }
        }
        // Si todas las fechas son válidas, aceptar el reto
        System.out.println("Reto aceptado: " + reto.getNombre());
    }
	
	public boolean retoSuperado(Reto reto, List<Sesion> sesiones) {
	    // Variables para almacenar el total de distancia o tiempo
	    double totalDistancia = 0;
	    double totalTiempo = 0;

	    // Filtrar las sesiones que están dentro del rango del reto
	    for (Sesion sesion : sesiones) {
	        if (!sesion.getFechaInicio().isBefore(reto.getFecha_inicio()) && !sesion.getFechaInicio().isAfter(reto.getFecha_fin())) {
	            // Si la sesión está dentro del rango, sumar su distancia o tiempo
	            if (reto.getDistancia() != 0) {
	                totalDistancia += sesion.getDistancia(); // Sumar distancia si el reto es de distancia
	            }
	            if (reto.getTiempo() != 0) {
	                totalTiempo += sesion.getTiempo(); // Sumar tiempo si el reto es de tiempo
	            }
	        }
	    }

	    // Verificar si se ha superado el reto (comparing with target values)
	    boolean retoSuperado = false;

	    if (reto.getDistancia() != 0 && totalDistancia >= reto.getDistancia()) {
	        retoSuperado = true;  // Si se ha superado la distancia
	    } else if (reto.getTiempo() != 0 && totalTiempo >= reto.getTiempo()) {
	        retoSuperado = true;  // Si se ha superado el tiempo
	    }

	    return retoSuperado;
	}

	
	public Map<Reto, Integer> RetosAceptados(List<Reto> ListaR) {
        Map<Reto, Integer> retosAceptados = new HashMap<>();
        for (Reto r : ListaR) {
            // Supongo que tienes un método que te dice si el reto está aceptado
            Integer progreso = calcularProgresoReto(r);
            retosAceptados.put(r, progreso);
        }
        return retosAceptados;
    }
	
	private Integer calcularProgresoReto(Reto reto) {
        // Aquí tendrías que usar la lógica para calcular el progreso, por ejemplo:
        double progreso = (reto.getDistancia() / (double) reto.getDistancia()) * 100;
        return (int) progreso;  // Retorna el porcentaje como entero
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
		        
		    	Set<Reto> retosAceptados = usuario.getRetosAceptados2();
		    	
		    	if (fechaFiltro != null) { 
		    		retosAceptados = retosAceptados.stream() .filter(reto -> reto.getFecha_inicio().equals(fechaFiltro)) .collect(Collectors.toSet()); 
		    		} 
		    	if (deporteFiltro != null && !deporteFiltro.isEmpty()) { 
		    		retosAceptados = retosAceptados.stream() .filter(reto -> reto.getDeporte().equalsIgnoreCase(deporteFiltro)) .collect(Collectors.toSet()); 
		    		}
		        
		        if (retosAceptados.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        } else {
		            List<Reto> listaRetos = new ArrayList<>(retosAceptados);
		            
		            int start = Math.max(listaRetos.size() - 5, 0);
		            List<Reto> ultimos5Retos = listaRetos.subList(start, listaRetos.size());
		            
		            System.out.println("Últimos 5 retos aceptados:");
		            for (Reto reto : ultimos5Retos) {
		                System.out.println(reto);
		            }
		        }

		        List<RetoDTO> dtos = new ArrayList<>();
		        retosAceptados.forEach(r -> dtos.add(retoToDTO(r)));

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
