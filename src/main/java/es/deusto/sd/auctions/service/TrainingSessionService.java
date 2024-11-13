package es.deusto.sd.auctions.service;


import es.deusto.sd.auctions.entity.Sesion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TrainingSessionService {

    private final List<Sesion> sesiones = new ArrayList<>();

    // Método para crear una nueva sesión
    public Sesion createSession(Sesion sesion) {
        sesiones.add(sesion);
        return sesion;
    }

    // Método para obtener las últimas 5 sesiones
    public List<Sesion> getRecentSessions() {
        int start = Math.max(sesiones.size() - 5, 0);
        return sesiones.subList(start, sesiones.size());
    }

    // Método para obtener sesiones en un rango de fechas
    public List<Sesion> getSessionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return sesiones.stream()
                .filter(sesion -> !sesion.getFechaInicio().isBefore(startDate) && !sesion.getFechaInicio().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
