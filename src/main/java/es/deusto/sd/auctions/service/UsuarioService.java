package es.deusto.sd.auctions.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import es.deusto.sd.auctions.dto.ProgresoRetoDTO;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;

public class UsuarioService {
	private final Map<Long, Usuario> usuarios = new HashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(0);

    public UsuarioService() {
        // Inicialización con datos de ejemplo
        usuarios.put(idGenerator.get(), new Usuario(idGenerator.incrementAndGet(), "Usuario1"));
        usuarios.put(idGenerator.get(), new Usuario(idGenerator.incrementAndGet(), "Usuario2"));
    }

    public Optional<Usuario> obtenerUsuario(Long usuarioId) {
        return Optional.ofNullable(usuarios.get(usuarioId));
    }

    public void aceptarReto(Long usuarioId, Long retoId) {
        obtenerUsuario(usuarioId).ifPresent(usuario -> usuario.aceptarReto(retoId));
    }

    public Set<Long> obtenerRetosAceptados(Long usuarioId) {
        return obtenerUsuario(usuarioId).map(Usuario::getRetosAceptados).orElse(new HashSet<>());
    }
    
    public List<ProgresoRetoDTO> obtenerProgresoRetosAceptados(Long usuarioId, List<Reto> listaRetos, List<Sesion> sesiones) {
        return obtenerUsuario(usuarioId).map(usuario -> {Set<Long> retosAceptados = usuario.getRetosAceptados();
                    return retosAceptados.stream().map(retoId -> calcularProgresoReto(retoId, listaRetos, sesiones)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
                }).orElse(Collections.emptyList());
    }
    
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
