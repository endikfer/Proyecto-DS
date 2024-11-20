package es.deusto.sd.auctions.service;

import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainingSessionService {

    private final List<Sesion> sesiones = new ArrayList<>();
    private Long Id = 1L;

    // Crear una nueva sesión
    public SesionDTO crearSesion(SesionDTO dto) {
        Sesion sesion = new Sesion();
        sesion.setId(Id++); 
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
    public List<SesionDTO> getSesionesRecientes() {
        List<SesionDTO> recentSessions = new ArrayList<>();

        int totalSessions = sesiones.size();
        int limit = totalSessions > 5 ? 5 : totalSessions; 

        for (int i = totalSessions - limit; i < totalSessions; i++) {
            Sesion sesion = sesiones.get(i); 
            recentSessions.add(toDTO(sesion));
        }

        return recentSessions;
    }


    // Obtener sesiones entre fechas
    public List<SesionDTO> getSesionesPorFecha(LocalDate startDate, LocalDate endDate) {
        List<SesionDTO> filteredSessions = new ArrayList<>();

        for (Sesion sesion : sesiones) {
            if (!sesion.getFechaInicio().isBefore(startDate) && !sesion.getFechaInicio().isAfter(endDate)) {
                filteredSessions.add(toDTO(sesion));
            }
        }

        return filteredSessions;
    }


    // Método para convertir Sesion a SesionDTO
    private SesionDTO toDTO(Sesion sesion) {
        SesionDTO dto = new SesionDTO();
        dto.setId(sesion.getId()); 
        dto.setTitulo(sesion.getTitulo());
        dto.setDeporte(sesion.getDeporte().name().toLowerCase()); 
        dto.setDistancia(sesion.getDistancia());
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setDuracion(sesion.getDuracion());
        return dto;
    }
}
