package es.deusto.sd.auctions.facade;

import es.deusto.sd.auctions.dto.UsuarioDTO;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<Usuario> usuarios = usuarioService.getUsuarios();
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
    public ResponseEntity<UsuarioDTO> registrarUsuario(
        @RequestParam("nombre") String nombre,
        @RequestParam("email") String email,
        @RequestParam("fecha_nac") String fecha_nac,
        @RequestParam("peso") float peso,
        @RequestParam("altura") int altura,
        @RequestParam("frec_car_max") int frec_car_max,
        @RequestParam("frec_car_rep") int frec_car_rep
    ) {
        try {
            usuarioService.registro(nombre,email,fecha_nac,peso,altura,frec_car_max,frec_car_rep);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Error de solicitud mal formada
        } catch (Exception e) {
            // Loguea el error y proporciona un mensaje genérico
            e.printStackTrace();  // O usa un logger
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // Error de servidor interno
        }
    }

    
    // Iniciar sesión
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión con su email y contraseña.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Inicio de sesión exitoso, se devuelve el token de autorización."),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Credenciales incorrectas."),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error: Error interno en el servidor.")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(
            @Parameter(name = "email", description = "Correo electrónico del usuario", required = true)
            @RequestParam String email,
            @Parameter(name = "contraseña", description = "Contraseña del usuario", required = true)
            @RequestParam String contraseña) {
        try {
            // Intentar iniciar sesión usando el servicio
            usuarioService.LogIn(email, contraseña);

            // Obtener el token generado para este usuario directamente del servicio
            Optional<String> tokenOpt = UsuarioService.getTokens().entrySet().stream()
                    .filter(entry -> entry.getValue().getEmail().equals(email))
                    .map(Map.Entry::getKey)
                    .findFirst();

            if (tokenOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales incorrectas o token no generado");
            }

            // Devolver el token generado
            return ResponseEntity.ok(tokenOpt.get());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al iniciar sesión");
        }
    }

    // Cerrar sesión
    @Operation(
            summary = "Cerrar sesión",
            description = "Permite a un usuario cerrar sesión proporcionando su token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Sesión cerrada correctamente."),
                    @ApiResponse(responseCode = "401", description = "Unauthorized: Token no válido o expirado."),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> cerrarSesion(
            @Parameter(name = "token", description = "Token de sesión del usuario", required = true)
            @RequestParam String token) {
        try {
            Usuario usuario = usuarioService.getUserByToken(token);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Token no válido o expirado.");
            }

            usuarioService.LogOut(usuario);
            return ResponseEntity.ok("Sesión cerrada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al cerrar sesión.");
        }
    }

    // Convertir un Usuario a UsuarioDTO
    private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getFecha_nac(),
                usuario.getPeso(),
                usuario.getAltura(),
                usuario.getFrec_car_max(),
                usuario.getFrec_car_rep()
        );
    }
}
