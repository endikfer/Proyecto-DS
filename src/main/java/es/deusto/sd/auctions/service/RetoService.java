package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.dto.RetoAcptadoDTO;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Reto;

@Service
public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();
	private long clave = 1L;
	private final Map<Long, List<RetoAcptadoDTO>> retoADTO = new HashMap<>();
	public TrainingSessionService TSS;

	public RetoService(TrainingSessionService TSS) {
		this.TSS = TSS;
	}
	
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
    
    public boolean aceptarReto(Long retoId, Long UsuId) {
        boolean acptado = false;
        
        if (!retoADTO.containsKey(UsuId)) {
        	retoADTO.put(UsuId, new ArrayList<>());
        }
        
        Reto r = obtenerReto(retoId);
        
        Integer d = r.getDistancia();
        Integer t = r.getTiempo();
        
        
        RetoAcptadoDTO a = new RetoAcptadoDTO(r.getId(), UsuId, r.getNombre(), r.getDeporte().name(), r.getFecha_inicio(), r.getFecha_fin(), d, t, 0.00);
        
        if (!retoADTO.get(UsuId).contains(a)) {
        	retoADTO.get(UsuId).add(a);
            acptado = true;
        }       
        
        return acptado;
    }
    
    public Double calcularProgresoReto(RetoAcptadoDTO r) {
        System.out.println("Calculo de progreso iniciado");

        // Obtiene las sesiones dentro del rango de fechas del reto
        List<SesionDTO> ListaS = TSS.getSesionesPorFecha(r.getFecha_inicio(), r.getFecha_fin());
        System.out.println("Sesiones1.0");
        if (ListaS == null) {
            ListaS = new ArrayList<>();
        }
        System.out.println("Sesiones");

        double p = 0.0; // Progreso acumulado (distancia o tiempo)
        double progreso = 0.0; // Porcentaje de progreso

        // Calcula el progreso basado en tiempo o distancia
        if (r.getTiempo() != null && r.getTiempo() > 0) {
            for (SesionDTO s : ListaS) {
            	if(r.getDeporte().toUpperCase().equals(s.getDeporte().toUpperCase())) {
                p += s.getDuracion(); // Suma la duración de las sesiones
            	}
            }
            progreso = p / r.getTiempo();
        } else if (r.getDistancia() != null && r.getDistancia() > 0) {
            for (SesionDTO s : ListaS) {
            	if(r.getDeporte().toUpperCase().equals(s.getDeporte().toUpperCase())) {
                p += s.getDistancia(); // Suma la distancia de las sesiones
            	}
            }
            progreso = p / r.getDistancia();
        }

        // Limita el progreso al 100%
        if (progreso > 1.0) {
            progreso = 1.0;
        }

        // Convierte el progreso a porcentaje
        progreso *= 100;

        System.out.println("Progreso calculado: " + progreso);
        return progreso;
    }
    
    public List<RetoAcptadoDTO> getRetosAceptados(Long UsuId) {
        System.out.println("Inicio de consulta de retos aceptados");

        // Obtiene la lista de retos aceptados para el usuario o un valor por defecto
        List<RetoAcptadoDTO> retosUsuario = retoADTO.getOrDefault(UsuId, new ArrayList<>());
        List<RetoAcptadoDTO> lista = new ArrayList<>();

        // Calcula el progreso de cada reto y agrega a la lista de resultados
        for (RetoAcptadoDTO r : retosUsuario) {
            // Clona el objeto para evitar modificar el original, si es necesario
            RetoAcptadoDTO nuevoReto = new RetoAcptadoDTO(r); // Constructor copia
            nuevoReto.setProgreso(calcularProgresoReto(nuevoReto));
            lista.add(nuevoReto);
            System.out.println("Progreso calculado para reto: " + nuevoReto.getId());
        }

        System.out.println("Consulta de retos aceptados finalizada");
        return lista;
    }
    
    
    
}