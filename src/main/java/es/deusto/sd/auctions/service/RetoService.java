package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.auctions.dto.ProgresoRetoDTO;
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
    
    public ProgresoRetoDTO calcularProgresoReto(Reto reto) {
        // Suponiendo que tenemos una lista de sesiones asociadas al reto
        List<Sesion> sesiones =  new ArrayList<Sesion>();//obtenerSesionesPorReto(reto); // Método que recupera sesiones del reto

        // Cálculo del progreso: aquí simplemente calculamos el porcentaje de avance
        double progreso = calcularProgreso(sesiones, reto);

        // Crear y devolver un DTO con el progreso del reto
        return new ProgresoRetoDTO(reto.getId(), progreso);
    }
    
    private double calcularProgreso(List<Sesion> sesiones, Reto reto) {
        double totalDistancia = reto.getDistancia(); // Distancia objetivo del reto
        double distanciaRecorrida = 0;

        // Sumar todas las distancias recorridas en las sesiones dentro del periodo del reto
        for (Sesion sesion : sesiones) {
            if (sesion.getFechaInicio().isAfter(reto.getFecha_inicio()) && sesion.getFechaInicio().isBefore(reto.getFecha_fin())) {
                distanciaRecorrida += sesion.getDistancia();
            }
        }

        // Calculamos el porcentaje de avance
        return (distanciaRecorrida / totalDistancia) * 100;
    }
}
