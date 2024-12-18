package es.deusto.sd.auctions.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.dao.UsuarioRepository;
import es.deusto.sd.auctions.entity.Login;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.external.ServiceGateway;
import es.deusto.sd.auctions.factory.FactoryGateway;

@Service
public class UsuarioService {
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    public static Map<String, Usuario> tokens = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private UsuarioRepository repository;
    private FactoryGateway factoria;
    
    public UsuarioService(UsuarioRepository user, FactoryGateway factoria) {
    	this.repository = user;
    	this.factoria = factoria;
    }

    public Optional<Usuario> obtenerUsuario(Long usuarioId) {
        return Optional.ofNullable(usuarios.get(usuarioId));
    }

    public Usuario getUserByToken(String token) {
        return tokens.get(token); 
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public void registro(String nombre, String email, Login tipo, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {

            Usuario u = new Usuario(nombre, email, tipo, fecha_nac, 0f, 0, 0, 0);

            if (peso > 0) u.setPeso(peso);
            if (altura > 0) u.setAltura(altura);
            if (frec_car_max > 0) u.setFrec_car_max(frec_car_max);
            if (frec_car_rep > 0) u.setFrec_car_rep(frec_car_rep);

            usuarios.put(u.getId(), u);
            repository.save(u);
    }

    public void logIn(String email, String contrasenia) {
        if (tokens.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
            return;
        }
        
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado.");
        }

        // Obtener el tipo de login del usuario
        Login tipoLogIn = usuario.getTipo();
        if (tipoLogIn == null) {
            throw new IllegalArgumentException("El tipo de login no está configurado para el usuario.");
        }

        ServiceGateway serviceGateway = factoria.factoria(tipoLogIn);

        /*if (serviceGateway.validateLogin(email, contrasenia)) {
            Usuario usuario2 = buscarUsuarioPorEmail(email);
            if (usuario2 != null) {
                generarToken(usuario2);
            } else {
                throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado.");
            }
        }*/
        
        if (serviceGateway.validateLogin(email, contrasenia)) {
                generarToken(usuario);
        }else {
            throw new IllegalArgumentException("Login fallido para el email: " + email);
        }
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarios.values().stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }

    public void generarToken(Usuario u) {
        String token = Timestamp.from(Instant.now()).toString();
        tokens.put(token, u);
    }

    public void LogOut(Usuario u) {
        tokens.entrySet().removeIf(entry -> entry.getValue().equals(u));
    }

    public static Map<String, Usuario> getTokens() {
        return tokens;
    }

    public Map<String, Usuario> obtenerTokens() {
        return tokens;
    }
    
}
