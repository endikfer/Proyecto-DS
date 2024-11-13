package es.deusto.sd.auctions.facade;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import es.deusto.sd.auctions.dto.CategoryDTO;
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
			description = "Returns a list of all available categories",
			responses = {
				@ApiResponse(responseCode = "200", description = "OK: List of categories retrieved successfully"),
				@ApiResponse(responseCode = "204", description = "No Content: No Categories found"),
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
				Scanner scanner = new Scanner(System.in);
		        System.out.print("Nombre del reto: ");
		        String nombre = scanner.nextLine();
		        System.out.print("Deporte a realizar(ciclismo o running): ");
		        String deporte = scanner.nextLine();
		        System.out.print("Fecha en la que inicia el reto: ");
		        String fecha_inicio = scanner.nextLine();
		        System.out.print("Fecha en la que termina el reto: ");
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
		        
		        try {
		            fecha_ini = LocalDate.parse(fecha_inicio);
		            fecha_f = LocalDate.parse(fecha_fin);
		        } catch (DateTimeParseException e) {
		            System.out.println("Formato de fecha incorrecto. Asegúrate de usar el formato yyyy-MM-dd.");
		        }
				
		        id++;
				Reto r= new Reto(id,nombre,deporte,fecha_ini,fecha_f, distancia,tiempo);
				usuario.añadirReto(r);
				System.out.println("Reto añadido correctamente.");
				usuario.mostrarRetos();
				
				if (categories.isEmpty()) {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
				return new ResponseEntity<>(dtos, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		private RetoDTO retoToDTO(Reto reto) {
			return new RetoDTO(reto.getNombre());
		}
}
