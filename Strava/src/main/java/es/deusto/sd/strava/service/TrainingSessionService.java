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
    private Long Id = 1L;
    private SesionRepository sesionrepo;

    // Crear una nueva sesión
    public void crearSesion(SesionDTO dto) {
        Sesion sesion = new Sesion();
        sesion.setId(Id++); 
        sesion.setTitulo(dto.getTitulo());
        sesion.setDeporte(Deporte.valueOf(dto.getDeporte().toUpperCase())); 
        sesion.setDistancia(dto.getDistancia());
        sesion.setFechaInicio(dto.getFechaInicio()); 
        sesion.setDuracion(dto.getDuracion());
        sesion.setTiempo(dto.getDuracion() * 60); 

        sesiones.add(sesion);
        sesionrepo.save(sesion);
    }

    // Obtener las 5 últimas sesiones
    public List<Sesion> getSesionesRecientes() {
        List<Sesion> recentSessions = new ArrayList<>();
        List<Sesion> Sessions = sesionrepo.findAll(); 
        for (int i = 0; i < Sessions.size(); i++) {
        	if (i<4) {
				recentSessions.add(Sessions.get(i));
			}
			
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
        dto.setFechaInicio(sesion.getFechaInicio());
        dto.setDuracion(sesion.getDuracion());
        return dto;
    }
}
