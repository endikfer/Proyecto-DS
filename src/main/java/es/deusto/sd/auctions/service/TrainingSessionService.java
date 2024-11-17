package es.deusto.sd.auctions.service;

import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.dto.CrearSesionDTO;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingSessionService {

    private final List<Sesion> sesiones = new ArrayList<>();
    private Long currentId = 1L;

    // Crear una nueva sesión
    public SesionDTO createSession(CrearSesionDTO dto) {
        Sesion sesion = new Sesion();
        sesion.setId(currentId++); 
        sesion.setTitulo(dto.getTitulo());
        sesion.setDeporte(Deporte.valueOf(dto.getDeporte().toUpperCase()));
        sesion.setDistancia(dto.getDistancia());
        sesion.setFechaInicio(dto.getFechaInicio()); 
        sesion.setDuracion(dto.getDuracion());
        sesion.setTiempo(dto.getDuracion() * 60); 

        sesiones.add(sesion);

        return toDTO(sesion);
    }

    // Obtener las 5 últimas sesiones
    public List<SesionDTO> getRecentSessions() {
        return sesiones.stream()
                .sorted(Comparator.comparing(Sesion::getFechaInicio).reversed())
                .limit(5)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener sesiones entre fechas
    public List<SesionDTO> getSessionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return sesiones.stream()
                .filter(s -> !s.getFechaInicio().isBefore(startDate) && !s.getFechaInicio().isAfter(endDate))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Método para convertir Sesion a SesionDTO
    private SesionDTO toDTO(Sesion sesion) {
        SesionDTO dto = new SesionDTO();
        dto.setTitulo(sesion.getTitulo());
        dto.setDeporte(sesion.getDeporte().name().toLowerCase());
        dto.setDistancia(sesion.getDistancia());
        dto.setFechaInicio(sesion.getFechaInicio()); 
        dto.setDuracion(sesion.getDuracion());
        return dto;
    }
}
