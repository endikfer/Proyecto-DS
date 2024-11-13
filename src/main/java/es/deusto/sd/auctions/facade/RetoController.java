package es.deusto.sd.auctions.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.RetoService;

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
}
