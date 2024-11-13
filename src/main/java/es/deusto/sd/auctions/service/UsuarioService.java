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

    public Set<Reto> obtenerRetosAceptados(Long usuarioId) {
        return obtenerUsuario(usuarioId).map(Usuario::getRetosAceptados2).orElse(new HashSet<>());
    }
    
    



}
