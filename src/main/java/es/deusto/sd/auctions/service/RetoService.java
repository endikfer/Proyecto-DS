package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Usuario;

public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();
	private final Map<Long, List<Long>> retosAceptados = new HashMap<>();
	private long clave = 8L;


	public RetoService() {
	    // Inicialización con datos de ejemplo usando el enumerador Deporte
	    retos.put(1L, new Reto(1L, "Reto 10K Running", Deporte.RUNNING, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0));
	    retos.put(2L, new Reto(2L, "Reto 50K Ciclismo", Deporte.CICLISMO, LocalDate.now(), LocalDate.now().plusDays(20), 50, 0));
	    retos.put(3L, new Reto(3L, "Reto Maratón Running", Deporte.RUNNING, LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0));
	    retos.put(4L, new Reto(4L, "Reto 2 Horas Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), 0, 120));
	    retos.put(5L, new Reto(5L, "Reto 30K Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0));
	    retos.put(6L, new Reto(6L, "Reto 1 Hora Running", Deporte.RUNNING, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60));
	    retos.put(7L, new Reto(7L, "Reto 20K Running", Deporte.RUNNING, LocalDate.now(), LocalDate.now().plusDays(15), 20, 0));
	}


    public Reto obtenerReto(Long retoId) {
        return retos.get(retoId);
    }

    public Collection<Reto> obtenerRetos() {
        return retos.values();
    }
    
    public void crearReto(long id, String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {		
		
    	Deporte deporteEnum;
        try {
            deporteEnum = Deporte.valueOf(deporte.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El deporte '" + deporte + "' no es válido. Los valores permitidos son: " + Arrays.toString(Deporte.values()));
        }
    	
        Reto reto = new Reto(id, nombre, deporteEnum, fecha_inicio, fecha_fin, distancia, tiempo);
		retos.put(clave, reto);
		clave++;
	}
    
    public boolean aceptarReto(Long retoId, Usuario usuario) {
        boolean acptado = false;
        
        if (!retosAceptados.containsKey(usuario.getId())) {
            retosAceptados.put(usuario.getId(), new ArrayList<>());
        }

        List<Long> retoIds = retosAceptados.get(usuario.getId());
        
        if (!retoIds.contains(retoId)) {
            retosAceptados.get(usuario.getId()).add(retoId);
            acptado = true;
        }
        
    	return acptado;
    }
    
    
    public List<Reto> getRetosAceptados(Usuario usuario){
    	List<Reto> lista = new ArrayList<Reto>();    	
    	List <Long> Ids = retosAceptados.get(usuario.getId());
    	
    	for(Long idr :Ids) {
    		lista.add(obtenerReto(idr));
    	}
    	
    	return lista;
    }
    
}