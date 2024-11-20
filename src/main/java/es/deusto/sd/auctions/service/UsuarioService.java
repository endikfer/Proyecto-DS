package es.deusto.sd.auctions.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import es.deusto.sd.auctions.entity.Usuario;

@Service
public class UsuarioService {
	private final Map<Long, Usuario> usuarios = new HashMap<>();
	public static Map<String, Usuario> tokens = new HashMap<>();
	public static Map<String, ArrayList<String>> serviciosExternos = new HashMap<>();
	
    public Optional<Usuario> obtenerUsuario(Long usuarioId) {
        return Optional.ofNullable(usuarios.get(usuarioId));
    }
    
    public Usuario getUserByToken(String token) {
        return tokens.get(token); 
    }
    
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }
    
    public void eliminarUsuario(Long id) {
        usuarios.remove(id);
    }
    
	public void registro(String nombre, String email, Date fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
		AtomicLong idGenerator = new AtomicLong(0);
	   	Usuario u = new Usuario(idGenerator.incrementAndGet(), nombre, email, fecha_nac);
	   	if (peso > 0) {
			u.setPeso(peso);
		}
	   	if (altura > 0) {
			u.setAltura(altura);
		}
	   	if (frec_car_max > 0) {
			u.setFrec_car_max(frec_car_max);
		}
	   	if (frec_car_rep > 0) {
			u.setFrec_car_rep(frec_car_rep);
		}
	   	int enc = 0;
	   	for (String se: serviciosExternos.keySet()) {
			for (String e: serviciosExternos.get(se)) {
				if (e.equals(email)) {
					enc = 1;
				}
			}
		}
	   	if (enc == 1) {
	   		usuarios.put(idGenerator.getAndIncrement(), u);
	   	}
	}
		
		public void LogIn(String email, String contrasenia) {
			//realizarComprobacionMeta(email, contrasenia)
			//realizarComprobacionGoogle(email, contrasenia)
			//enviar datos a Meta o Google y realizar comprobacion
			int enc = 0;
			Usuario usuario = null;
			for (String token: tokens.keySet()) {
				Usuario u = tokens.get(token);
				if (u.getEmail().equals(email)) {
					enc = 1;
					usuario = u;
				}
			}
			if (enc == 0) {
				generarToken(usuario);
			}
		}
		public void generarToken(Usuario u) {
			String token = Timestamp.from(Instant.now()).toString();
			tokens.put(token, u);
		}
		public void LogOut(Usuario u) {
			for (String token: tokens.keySet()) {
				if (tokens.get(token).equals(u)) {
					tokens.remove(token);
				}
			}
		}
	
	}
