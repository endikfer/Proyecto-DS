package es.deusto.sd.auctions.facade;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import es.deusto.sd.auctions.entity.Usuario;

public class UsuarioService {
	private final Map<Long, Usuario> usuarios = new HashMap<>();

    public UsuarioService() {
        // Inicialización con datos de ejemplo
        usuarios.put(1L, new Usuario(1L, "Usuario1"));
        usuarios.put(2L, new Usuario(2L, "Usuario2"));
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
}
