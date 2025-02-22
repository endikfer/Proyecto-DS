package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.UsuarioDTO;
import es.deusto.sd.strava.entity.Usuario;
import es.deusto.sd.strava.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios Controller", description = "Endpoints para gestionar los usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve una lista con todos los usuarios registrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Lista de usuarios devuelta exitosamente"),
                    @ApiResponse(responseCode = "204", description = "No Content: No hay usuarios registrados"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UsuarioDTO> usuariosDTO = usuarios.stream()
                    .map(this::convertirUsuarioADTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(usuariosDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Obtener la lista de tokens",
            description = "Devuelve un mapa con los tokens activos y sus usuarios asociados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Lista de tokens devuelta exitosamente."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @GetMapping("/tokens")
    public ResponseEntity<Map<String, UsuarioDTO>> obtenerTokens() {
        try {
            Map<String, Usuario> tokens = usuarioService.obtenerTokens();

            Map<String, UsuarioDTO> tokensDTO = tokens.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> convertirUsuarioADTO(entry.getValue())
                    ));

            return new ResponseEntity<>(tokensDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Registra un nuevo usuario con los datos proporcionados.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created: Usuario registrado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Datos inválidos o incompletos"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/registro")
    public ResponseEntity<Void> registrarUsuario(
        @RequestParam("nombre") String nombre,
        @RequestParam("email") String email,
        @RequestParam("tipo") String tipo,
        @RequestParam("fecha_nac") String fecha_nac,
        @RequestParam("peso") float peso,
        @RequestParam("altura") int altura,
        @RequestParam("frec_car_max") int frec_car_max,
        @RequestParam("frec_car_rep") int frec_car_rep
    ) {
        try {
            usuarioService.registro(nombre,email, tipo, fecha_nac,peso,altura,frec_car_max,frec_car_rep);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión verificando si tiene un token asignado. Si no, genera uno nuevo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Inicio de sesión exitoso, se devuelve el token."),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Usuario no registrado o datos incorrectos."),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials, login failed"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "contraseña") String contraseña) {
        try {
        	Optional<String> token = usuarioService.logIn(email, contraseña);

            if (token.isPresent()) {
            	return new ResponseEntity<>(token.get(), HttpStatus.OK);
            } else {
            	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Permite a un usuario cerrar sesión eliminando su token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Sesión cerrada correctamente."),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Usuario no tiene un token válido."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(
            @Parameter(name = "token", description = "Token de sesión del usuario", required = true)
            @RequestBody String token) {
        try {
            if (token == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Optional<Boolean> result = usuarioService.logout(token);
            
            if (result.isPresent() && result.get()) {
            	return new ResponseEntity<>(HttpStatus.OK);	
            } else {
            	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }  

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
        return new UsuarioDTO(
        		usuario.getNombre(), 
        		usuario.getEmail(), 
        		usuario.getTipo(), 
        		usuario.getFecha_nac(), 
        		usuario.getPeso(), 
        		usuario.getAltura(),
        		usuario.getFrec_car_max(), 
        		usuario.getFrec_car_rep()
        	);
    }
}
