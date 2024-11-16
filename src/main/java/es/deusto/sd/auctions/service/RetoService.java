package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.auctions.entity.Reto;

public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();
	private long clave = 3L;


    public RetoService() {
        // Inicialización con datos de ejemplo
        retos.put(1L, new Reto(1L, "Reto 10K Running", "running", LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0));
        retos.put(2L, new Reto(2L, "Reto 50K Ciclismo", "ciclismo", LocalDate.now(), LocalDate.now().plusDays(20) ,50, 0));
    }

    public Optional<Reto> obtenerReto(Long retoId) {
        return Optional.ofNullable(retos.get(retoId));
    }

    public Collection<Reto> obtenerRetos() {
        return retos.values();
    }
    
    public void crearReto(long id, String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {		
		// Create a new bid and associate it with the article
		Reto reto = new Reto(id, nombre, deporte, fecha_inicio, fecha_fin, distancia, tiempo);
		retos.put(clave, reto);
		clave++;
	}
}