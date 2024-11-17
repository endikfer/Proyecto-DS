package es.deusto.sd.auctions.service;

	import java.sql.Date;
	import java.sql.Timestamp;
	import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.HashMap;
import java.util.List;
import java.util.Map;
	import java.util.Optional;
	import java.util.concurrent.atomic.AtomicLong;
	import es.deusto.sd.auctions.entity.Usuario;

public class UsuarioService {
	private final Map<Long, Usuario> usuarios = new HashMap<>();
	private static Map<String, Usuario> tokens = new HashMap<>();
	private static Map<String, ArrayList<String>> serviciosExternos = new HashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(0);

    public UsuarioService() {
        // Inicialización con datos de ejemplo
    	// Crear fechas usando LocalDate y convertirlas a Date
        Date fecha1 = (Date) Date.from(LocalDate.of(1990, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fecha2 = (Date) Date.from(LocalDate.of(1988, 6, 22).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fecha3 = (Date) Date.from(LocalDate.of(1992, 3, 5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fecha4 = (Date) Date.from(LocalDate.of(1985, 12, 30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Crear usuarios
        Usuario user1 = new Usuario("Juan Pérez", "info@google.com", fecha1, 70.5f, 175, 190, 60);
        Usuario user2 = new Usuario("Ana López", "contact@meta.com", fecha2, 62.0f, 165, 180, 55);
        Usuario user3 = new Usuario("Carlos Díaz", "support@google.com", fecha3, 80.0f, 180, 195, 65);
        Usuario user4 = new Usuario("María Gómez", "help@meta.com", fecha4, 68.0f, 170, 185, 58);

        // Asignar usuarios al mapa
        usuarios.put(idGenerator.getAndIncrement(), user1);
        usuarios.put(idGenerator.getAndIncrement(), user2);
        usuarios.put(idGenerator.getAndIncrement(), user3);
        usuarios.put(idGenerator.getAndIncrement(), user4);

        // Asignar tokens
        tokens.put("2024-11-12 14:35:12.123", user1);
        tokens.put("2024-11-12 14:35:12.456", user2);
        tokens.put("2024-11-12 14:35:12.789", user3);
        tokens.put("2024-11-12 14:35:13.012", user4);

        // Servicios externos
        serviciosExternos.put("Google", new ArrayList<>(Arrays.asList("support@google.com", "info@google.com")));
        serviciosExternos.put("Meta", new ArrayList<>(Arrays.asList("help@meta.com", "contact@meta.com")));
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
    
    public void eliminarUsuario(Long id) {
        usuarios.remove(id);
    }
    
	public Usuario registro(String nombre, String email, Date fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
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
	   	if (enc == 1) return u;
		else return null;
		}
		
		public String LogIn(String email, String contrasenia) {
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
				return generarToken(usuario);
			}else {
				return null;
			}
		}
		public String generarToken(Usuario u) {
			String token = Timestamp.from(Instant.now()).toString();
			tokens.put(token, u);
			return token;
		}
		public void LogOut(Usuario u) {
			for (String token: tokens.keySet()) {
				if (tokens.get(token).equals(u)) {
					tokens.remove(token);
				}
			}
		}
	
	}
