package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.dao.RetoRepository;
import es.deusto.sd.auctions.dto.RetoAcptadoDTO;
import es.deusto.sd.auctions.dto.RetoDTO;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Reto;

@Service
public class RetoService {
	private final Map<Long, List<RetoAcptadoDTO>> retoADTO = new HashMap<>();
	public TrainingSessionService TSS;
	public RetoRepository retorepo;

	public RetoService(TrainingSessionService TSS, RetoRepository retorepo) {
		this.TSS = TSS;
		this.retorepo = retorepo;
	}
	
    public Reto obtenerReto(Long retoId) {
    	Optional<Reto> retoOptional = retorepo.findById(retoId);
    	Reto reto = retoOptional.get();
        return reto;
    }

    public Collection<Reto> obtenerRetos() {
        return retorepo.findAll();
    }
    
    public void crearReto(String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {		
		
    	Deporte deporteEnum;
        try {
            deporteEnum = Deporte.valueOf(deporte.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El deporte '" + deporte + "' no es válido. Los valores permitidos son: " + Arrays.toString(Deporte.values()));
        }
    	
        Reto reto = new Reto(nombre, deporteEnum, fecha_inicio, fecha_fin, distancia, tiempo);
		retorepo.save(reto);
	}
    
    public List<RetoDTO> obtenerRetos(String deporteFiltro, String fechaFiltroStr) {
        try {

            // Obtiene la lista de retos
            Collection<Reto> retos = obtenerRetos();

            // Convierte la fecha desde el parámetro si se pasa, sino usa la fecha actual
            LocalDate fechaBusqueda = (fechaFiltroStr != null && !fechaFiltroStr.isEmpty()) ? LocalDate.parse(fechaFiltroStr) : LocalDate.now();

            // Filtra los retos por fecha
            List<Reto> retosFiltradosPorFecha = new ArrayList<>();
            for (Reto reto : retos) {
                if (reto.getFecha_fin().isAfter(fechaBusqueda)) {
                    retosFiltradosPorFecha.add(reto);
                }
            }

            // Filtra los retos por deporte si se proporciona
            List<Reto> retosFiltradosPorDeporte = new ArrayList<>();
            if (deporteFiltro != null && !deporteFiltro.isEmpty()) {
                for (Reto reto : retosFiltradosPorFecha) {
                    if (reto.getDeporte().name().equalsIgnoreCase(deporteFiltro)) {
                        retosFiltradosPorDeporte.add(reto);
                    }
                }
            }

            // Si no se filtra por deporte, mantén los retos filtrados solo por fecha
            List<Reto> resultadosFinales = (deporteFiltro != null && !deporteFiltro.isEmpty()) ? retosFiltradosPorDeporte : retosFiltradosPorFecha;

            // Si no hay resultados, devuelve lista vacía
            if (resultadosFinales.isEmpty()) {
                return Collections.emptyList();
            }

            // Ordena los resultados por la fecha de inicio
            List<Reto> retosOrdenados = new ArrayList<>(resultadosFinales);
            retosOrdenados.sort((r1, r2) -> r2.getFecha_inicio().compareTo(r1.getFecha_inicio()));

            // Devuelve los últimos 5 retos (o menos, si hay menos de 5)
            int maxRetos = Math.min(retosOrdenados.size(), 5);
            List<Reto> ultimosRetos = retosOrdenados.subList(0, maxRetos);

            // Convierte los retos a DTOs
            List<RetoDTO> dtos = new ArrayList<>();
            for (Reto reto : ultimosRetos) {
                dtos.add(retoToDTO(reto));
            }

            return dtos;
        } catch (Exception e) {
            // En caso de error, retorna una lista vacía
            return Collections.emptyList();
        }
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

        // Obtiene las sesiones dentro del rango de fechas del reto
        List<SesionDTO> ListaS = TSS.getSesionesPorFecha(r.getFecha_inicio().toString(), r.getFecha_fin().toString());
        if (ListaS == null) {
            ListaS = new ArrayList<>();
        }

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

        return progreso;
    }
    
    public List<RetoAcptadoDTO> getRetosAceptados(Long UsuId) {

        // Obtiene la lista de retos aceptados para el usuario o un valor por defecto
        List<RetoAcptadoDTO> retosUsuario = retoADTO.getOrDefault(UsuId, new ArrayList<>());
        List<RetoAcptadoDTO> lista = new ArrayList<>();

        // Calcula el progreso de cada reto y agrega a la lista de resultados
        for (RetoAcptadoDTO r : retosUsuario) {
            // Clona el objeto para evitar modificar el original, si es necesario
            RetoAcptadoDTO nuevoReto = new RetoAcptadoDTO(r); // Constructor copia
            nuevoReto.setProgreso(calcularProgresoReto(nuevoReto));
            lista.add(nuevoReto);
        }

        return lista;
    }
    
    private RetoDTO retoToDTO(Reto reto) {
		return new RetoDTO( 
				reto.getNombre(),
				reto.getDeporte().name().toLowerCase(),
				reto.getFecha_inicio(),
				reto.getFecha_fin(),
				reto.getDistancia(), 
				reto.getTiempo());
	}
}