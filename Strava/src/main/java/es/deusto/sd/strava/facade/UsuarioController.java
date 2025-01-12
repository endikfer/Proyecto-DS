package es.deusto.sd.strava.facade;

import es.deusto.sd.strava.dto.UsuarioDTO;
import es.deusto.sd.strava.entity.Login;
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

    // Obtener todos los usuarios
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
    
    // Obtener la lista de tokens
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
            // Obtener los tokens del servicio
            Map<String, Usuario> tokens = usuarioService.obtenerTokens();

            // Convertir los valores de Usuario a UsuarioDTO
            Map<String, UsuarioDTO> tokensDTO = tokens.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> convertirUsuarioADTO(entry.getValue())
                    ));

            return new ResponseEntity<>(tokensDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // Loguear el error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Respuesta con error 500
        }
    }
    
    // Registrar un nuevo usuario
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
        @RequestParam("tipo") Login tipo,
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Error de solicitud mal formada
        } catch (Exception e) {
            // Loguea el error y proporciona un mensaje genérico
            e.printStackTrace();  // O usa un logger
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Error de servidor interno
        }
    }

    
 // Iniciar sesión (PUT)
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión verificando si tiene un token asignado. Si no, genera uno nuevo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Inicio de sesión exitoso, se devuelve el token."),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Usuario no registrado o datos incorrectos."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @PutMapping("/login")
    public ResponseEntity<Void> iniciarSesion(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "contraseña") String contraseña) {
        try {
            // Llamada al servicio para hacer login
            usuarioService.logIn(email, contraseña);

            // Buscar el token asociado al usuario
            Optional<String> tokenOpt = UsuarioService.getTokens().entrySet().stream()
                    .filter(entry -> entry.getValue().getEmail().equals(email))
                    .map(Map.Entry::getKey)
                    .findFirst();
            
            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Error 400 sin cuerpo
            }
            // Enviar una respuesta exitosa sin contenido (código 200)
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Error 400 sin cuerpo
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error 500 sin cuerpo
        }
    }



    // Cerrar sesión (DELETE)
    @Operation(
            summary = "Cerrar sesión",
            description = "Permite a un usuario cerrar sesión eliminando su token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Sesión cerrada correctamente."),
                    @ApiResponse(responseCode = "400", description = "Bad Request: Usuario no tiene un token válido."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @DeleteMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(
            @Parameter(name = "token", description = "Token de sesión del usuario", required = true)
            @RequestParam(name = "token") String token) {
        try {
            // Validar si el token existe
            Usuario usuario = usuarioService.getUserByToken(token);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Token no válido
            }
            // Eliminar el token del mapa de tokens
            usuarioService.LogOut(usuario); // Eliminar el token asociado

            return ResponseEntity.ok().build(); // Respuesta exitosa sin contenido
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Error 500 sin contenido
        }
    }

    // Convertir un Usuario a UsuarioDTO
    private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipo().getName(),
                usuario.getFecha_nac(),
                usuario.getPeso(),
                usuario.getAltura(),
                usuario.getFrec_car_max(),
                usuario.getFrec_car_rep()
        );
    }
}
