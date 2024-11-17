package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.auctions.entity.Reto;

public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();
	private long clave = 8L;


    public RetoService() {
        // Inicialización con datos de ejemplo
        retos.put(1L, new Reto(1L, "Reto 10K Running", "running", LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0));
        retos.put(2L, new Reto(2L, "Reto 50K Ciclismo", "ciclismo", LocalDate.now(), LocalDate.now().plusDays(20) ,50, 0));
        retos.put(3L, new Reto(3L, "Reto Maratón Running", "running", LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0));
        retos.put(4L, new Reto(4L, "Reto 2 Horas Ciclismo", "ciclismo", LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), 0, 120));
        retos.put(5L, new Reto(5L, "Reto 30K Ciclismo", "ciclismo", LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0));
        retos.put(6L, new Reto(6L, "Reto 1 Hora Running", "running", LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60));
        retos.put(7L, new Reto(7L, "Reto 20K Running", "running", LocalDate.now(), LocalDate.now().plusDays(15), 20, 0));
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