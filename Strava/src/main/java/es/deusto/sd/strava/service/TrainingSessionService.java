package es.deusto.sd.strava.service;

import es.deusto.sd.strava.dao.SesionRepository;
import es.deusto.sd.strava.dto.SesionDTO;
import es.deusto.sd.strava.entity.Deporte;
import es.deusto.sd.strava.entity.Sesion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TrainingSessionService {

    private final List<Sesion> sesiones = new ArrayList<>();
    private Long id = 1L;
    private SesionRepository sesionrepo;
    
    public TrainingSessionService(SesionRepository sesionrepo) {
        this.sesionrepo = sesionrepo;
    }
    // Crear una nueva sesión
    public void crearSesion(SesionDTO dto) {
        Sesion sesion = new Sesion();
        sesion.setId(id++); 
        sesion.setTitulo(dto.getTitulo());
        sesion.setDeporte(Deporte.valueOf(dto.getDeporte().toUpperCase())); 
        sesion.setDistancia(dto.getDistancia());
        sesion.setFechaInicio(LocalDate.parse(dto.getFechaInicio())); 
        sesion.setDuracion(dto.getDuracion());
    }

    public void crearSesion(Sesion sesion) {
        sesiones.add(sesion);
        sesionrepo.save(sesion);
    }

    // Obtener las 5 últimas sesiones
    public List<Sesion> getSesionesRecientes() {
        List<Sesion> sessions = sesionrepo.findAll();

        int totalSessions = sessions.size();
        int limit = totalSessions > 5 ? 5 : totalSessions; 
        
        List<Sesion> recentSessions = new ArrayList<>();
        for (int i = totalSessions - limit; i < totalSessions; i++) {
            Sesion sesion = sessions.get(i); 
            recentSessions.add(sesion);
        }

        return recentSessions;
    }


    // Obtener sesiones entre fechas
    public List<SesionDTO> getSesionesPorFecha(String startDate, String endDate) {
        List<SesionDTO> filteredSessions = new ArrayList<>();
        LocalDate fechaInicio = LocalDate.parse(startDate);
        LocalDate fechaFin = LocalDate.parse(endDate);
        for (Sesion sesion : sesiones) {
            if (!sesion.getFechaInicio().isBefore(fechaInicio) && !sesion.getFechaInicio().isAfter(fechaFin)) {
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
        dto.setFechaInicio(sesion.getFechaInicio().toString());
        dto.setDuracion(sesion.getDuracion());
        return dto;
    }
}
