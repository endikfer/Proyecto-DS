package es.deusto.sd.auctions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.deusto.sd.auctions.dto.RetoAcptadoDTO;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Usuario;

public class RetoService {

	private final Map<Long, Reto> retos = new HashMap<>();
	private final Map<Long, List<Long>> retosAceptados = new HashMap<>();
	private long clave = 1L;
	private final Map<List<Long>, RetoAcptadoDTO> retoADTO = new HashMap<>();
	public TrainingSessionService TSS;

    public Reto obtenerReto(Long retoId) {
        return retos.get(retoId);
    }

    public Collection<Reto> obtenerRetos() {
        return retos.values();
    }
    public RetoAcptadoDTO obtenerRetoAceptadoDTO(Long retoId, Long UsuId) {
    	List<Long> l = new ArrayList<Long>();
    	l.add(retoId);
    	l.add(UsuId);
    	return retoADTO.get(l);
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
        
        if (!retosAceptados.containsKey(UsuId)) {
            retosAceptados.put(UsuId, new ArrayList<>());
        }

        List<Long> retoIds = retosAceptados.get(UsuId);
        
        if (!retoIds.contains(retoId)) {
            retosAceptados.get(UsuId).add(retoId);
            acptado = true;
        }
        
        Reto r = obtenerReto(retoId);
        
        Integer d = r.getDistancia();
        Integer t = r.getTiempo();
        
        
        RetoAcptadoDTO a = new RetoAcptadoDTO(r.getId(), UsuId, r.getNombre(), r.getDeporte().name(), r.getFecha_inicio(), r.getFecha_fin(), d, t, 0.00);
        
        List<Long> l = new ArrayList<Long>();
        l.add(retoId);
        l.add(a.getId());
        retoADTO.put(l, a);
        
    	return acptado;
    }
    
    public Double calcularProgresoReto(RetoAcptadoDTO r) {
    	
    	List<SesionDTO> ListaS = TSS.getSesionesPorFecha(r.getFecha_inicio(), r.getFecha_fin());
    	Double progreso;
    	int p = 0;
    	
    	if(r.getTiempo()!=null) {
    		for (SesionDTO s : ListaS) {
    			p += s.getDuracion();
			}
    		progreso = (double) (p/r.getTiempo());
    	}else {
    		for (SesionDTO s : ListaS) {
    			p += s.getDistancia();
			}
    		progreso = (double) (p/r.getDistancia());
    	}
    	if (progreso > 1.0) {
            progreso = 1.0;
        }
    	progreso *= 100;
		return progreso;
    }
    
    public List<RetoAcptadoDTO> getRetosAceptados(Long UsuId){
    	List<RetoAcptadoDTO> lista = new ArrayList<RetoAcptadoDTO>();    	
    	List <Long> Ids = retosAceptados.get(UsuId);
    	
    	for(Long idr :Ids) {
    		RetoAcptadoDTO r = obtenerRetoAceptadoDTO(idr,UsuId);
    		r.setProgreso(calcularProgresoReto(r));
    		lista.add(r);
    	}
    	
    	return lista;
    }
    
}