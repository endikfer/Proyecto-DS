package es.deusto.sd.auctions.facade;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import es.deusto.sd.auctions.dto.ProgresoRetoDTO;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.UsuarioService;

public class UsuarioConroller {
	
	UsuarioService US;

	public UsuarioConroller(UsuarioService US) {
		this.US = US;
	}
	
	public List<ProgresoRetoDTO> obtenerProgresoRetosAceptados(Long usuarioId, List<Reto> listaRetos, List<Sesion> sesiones) {
        return obtenerUsuario(usuarioId).map(usuario -> {
            Set<Reto> retosAceptados = usuario.getRetosAceptados2();
            return retosAceptados.stream()
                    .map(reto -> calcularProgresoReto(reto, sesiones))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }
	
	//Va en reto Controller
	private ProgresoRetoDTO calcularProgresoReto(Reto reto, List<Sesion> sesiones) {
        // Filtrar sesiones relevantes para el reto
        List<Sesion> sesionesFiltradas = sesiones.stream()
                .filter(s -> s.getDeporte().equalsIgnoreCase(reto.getDeporte()))
                .filter(s -> !s.getFechaInicio().toLocalDate().isBefore(reto.getFecha_fin()) && 
                        !s.getFechaInicio().toLocalDate().isAfter(reto.getFecha_fin()))
                .collect(Collectors.toList());

        // Calcular progreso
        double progreso = 0.0;
        if (reto.getDistancia() > 0) { // Reto de distancia
            double distanciaTotal = sesionesFiltradas.stream().mapToDouble(Sesion::getDistancia).sum();
            progreso = (distanciaTotal / reto.getDistancia()) * 100;
        } else if (reto.getTiempo() > 0) { // Reto de tiempo
            double tiempoTotal = sesionesFiltradas.stream().mapToDouble(Sesion::getDuracion).sum();
            progreso = (tiempoTotal / reto.getTiempo()) * 100;
        }

        // Limitar progreso al 100%
        progreso = Math.min(progreso, 100.0);

        // Crear y devolver el DTO con la información del progreso
        return new ProgresoRetoDTO(reto.getId(), reto.getNombre(), reto.getDeporte(), progreso);
    }
	
}
