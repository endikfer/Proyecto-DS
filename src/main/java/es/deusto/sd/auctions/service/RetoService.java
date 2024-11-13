package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;

public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();

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
    
   
}
