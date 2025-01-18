package es.deusto.sd.strava.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

import es.deusto.sd.strava.dao.UsuarioRepository;
import es.deusto.sd.strava.entity.Login;
import es.deusto.sd.strava.entity.Usuario;
import es.deusto.sd.strava.external.ServiceGateway;
import es.deusto.sd.strava.factory.FactoryGateway;

@Service
public class UsuarioService {
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    public static Map<String, Usuario> tokens = new HashMap<>();
    private UsuarioRepository repository;
    private FactoryGateway factoria;
    
    public UsuarioService(UsuarioRepository user, FactoryGateway factoria) {
    	this.repository = user;
    	this.factoria = factoria;
    }

    public Usuario getUserByToken(String token) {
        return tokens.get(token); 
    }
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    public List<Usuario> obtenerTodosLosUsuarios() {
        return repository.findAll();
    }

    public void registro(String nombre, String email, String tipo, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
           
            Usuario usuario = repository.findByEmail(email);
            if (usuario != null) {
                throw new IllegalArgumentException("Usuario con el email proporcionado ya existe.");
            }
            Usuario u = new Usuario(nombre, email, Login.valueOf(tipo), fecha_nac, 0f, 0, 0, 0);

            if (peso > 0) u.setPeso(peso);
            if (altura > 0) u.setAltura(altura);
            if (frec_car_max > 0) u.setFrec_car_max(frec_car_max);
            if (frec_car_rep > 0) u.setFrec_car_rep(frec_car_rep);
            
            usuarios.put(u.getId(), u);
            repository.save(u);
    }

    public Optional<String> logIn(String email, String contrasenia) {
    	String token;
        if (tokens.values().stream().anyMatch(u -> u.getEmail().equals(email))) {
            return Optional.empty();
        }
        
        Usuario usuario = repository.findByEmail(email);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado.");
        }

        Login tipoLogIn = usuario.getTipo();
        if (tipoLogIn == null) {
            throw new IllegalArgumentException("El tipo de login no está configurado para el usuario.");
        }

        ServiceGateway serviceGateway = factoria.factoria(tipoLogIn);
        
        if (serviceGateway.validateLogin(email, contrasenia)) {
                token = generarToken(usuario);
                System.out.println(tokens);
                return Optional.of(token);
        }else {

            throw new IllegalArgumentException("Login fallido para el email: " + email);
        }
    }

    public String generarToken(Usuario u) {
        String token = Timestamp.from(Instant.now()).toString();
        tokens.put(token, u);
        return token;
    }

    public Optional<Boolean> logout(String token) {
        if (tokens.containsKey(token)) {
            tokens.remove(token);
            System.out.println(tokens);
            return Optional.of(true);
        } else {
            return Optional.empty();
        }

    }
        
    public static Map<String, Usuario> getTokens() {
        return tokens;
    }

    public Map<String, Usuario> obtenerTokens() {
        return tokens;
    }
    
}
