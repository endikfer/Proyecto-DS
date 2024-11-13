package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.RetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public class RetoController {
	private final Map<Long, Usuario> usuarios = new HashMap<>();
	private final RetoService RS;
	public RetoController(RetoService RS) {
		this.RS = RS;
	}
	
	public void aceptarReto(Reto r, List<Sesion> ListaSesion) {
		for (Sesion s : ListaSesion) {
			if(true) {//Mirar que la fecha de inicio sea mayor que la fecha de inicio y lo mismo con la fecha final
				
			}
		}
		//Aceptar el reto
		
	}
	
	public Map<Reto, Integer> RetosAceptados(List<Reto> ListaR){
		Map<Reto, Integer> retosAceptados = new HashMap<>();
		Integer progreso = 0;
		for (Reto r : ListaR) {
			if(true) {//comprobar que estan los retos aceptados por el usuario
				retosAceptados.put(r, progreso);
				//coger el pregreso y meterlo
			}
		}
		return retosAceptados;
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
		public ResponseEntity<List<RetoDTO>> crearReto(Usuario usuario) {
		    if (!usuario.estaAutenticado()) {
		        System.out.println("El usuario no ha iniciado sesión.");
		        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    }

		    try {
		        Long id = 0L;
		        LocalDate fecha_ini;
		        LocalDate fecha_f;
		        Integer distancia = null;
		        Integer tiempo = null;
		        
		        @SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
		        System.out.print("Nombre del reto: ");
		        String nombre = scanner.nextLine();
		        System.out.print("Deporte a realizar (ciclismo o running): ");
		        String deporte = scanner.nextLine();
		        System.out.print("Fecha en la que inicia el reto (yyyy-MM-dd): ");
		        String fecha_inicio = scanner.nextLine();
		        System.out.print("Fecha en la que termina el reto (yyyy-MM-dd): ");
		        String fecha_fin = scanner.nextLine();
		        System.out.print("¿Cuál es el objetivo del reto? (Escriba 'distancia' o 'tiempo'): ");
		        String objetivo = scanner.nextLine();

		        if ("distancia".equalsIgnoreCase(objetivo)) {
		            System.out.print("Distancia (km): ");
		            distancia = Integer.parseInt(scanner.nextLine());
		        } else if ("tiempo".equalsIgnoreCase(objetivo)) {
		            System.out.print("Tiempo (mins): ");
		            tiempo = Integer.parseInt(scanner.nextLine());
		        } else {
		            System.out.println("Objetivo no válido. Debe ser 'distancia' o 'tiempo'.");
		            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		        }
		        
		        fecha_ini = LocalDate.parse(fecha_inicio);
		        fecha_f = LocalDate.parse(fecha_fin);
		        
		        id++;
		        Reto reto = new Reto(id, nombre, deporte, fecha_ini, fecha_f, distancia, tiempo);
		        usuario.añadirReto(reto);
		        System.out.println("Reto añadido correctamente.");

		        // Obtener la lista de retos del usuario
		        Set<Reto> retos = usuario.getRetosAceptados2();
		        
		        if (retos.isEmpty()) {
		            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		        }

		        // Convertir la lista de Reto a RetoDTO
		        List<RetoDTO> dtos = new ArrayList<>();
		        retos.forEach(r -> dtos.add(retoToDTO(r)));

		        return new ResponseEntity<>(dtos, HttpStatus.OK);
		    } catch (DateTimeParseException e) {
		        System.out.println("Formato de fecha incorrecto. Asegúrate de usar el formato yyyy-MM-dd.");
		        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
		public ResponseEntity<List<RetoDTO>> consultarReto(Usuario usuario) {
		    if (!usuario.estaAutenticado()) {
		        System.out.println("El usuario no ha iniciado sesión.");
		        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		    }

		    try {
		        
		    	Set<Reto> retosAceptados = usuario.getRetosAceptados2();
		        
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
			return new RetoDTO(reto.getNombre());
		}
}
