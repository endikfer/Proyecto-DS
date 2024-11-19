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
	private long clave = 1L;

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