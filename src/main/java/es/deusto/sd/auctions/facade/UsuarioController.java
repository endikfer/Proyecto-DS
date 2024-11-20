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

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Devuelve los datos del usuario correspondiente al ID proporcionado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Usuario encontrado"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(
            @Parameter(name = "id", description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
            if (usuario.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(convertirUsuarioADTO(usuario.get()), HttpStatus.OK);
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
    @PostMapping
    public ResponseEntity<Void> registrarUsuario(
            @Parameter(name = "usuarioDTO", description = "Datos del usuario a registrar", required = true)
            @RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.registro(
                    usuarioDTO.getNombre(),
                    usuarioDTO.getEmail(),
                    usuarioDTO.getFecha_nac(),
                    usuarioDTO.getPeso(),
                    usuarioDTO.getAltura(),
                    usuarioDTO.getFrec_car_max(),
                    usuarioDTO.getFrec_car_rep()
            );
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Eliminar un usuario por ID",
            description = "Elimina el usuario correspondiente al ID proporcionado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK: Usuario eliminado"),
                    @ApiResponse(responseCode = "404", description = "Not Found: Usuario no encontrado"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(name = "id", description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            if (usuarioService.obtenerUsuario(id).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    	        // Buscar el token generado para este usuario
    	        Usuario usuario = usuarioService.getUsuarios().stream()
    	                .filter(u -> u.getEmail().equals(email))
    	                .findFirst()
    	                .orElse(null);

    	        if (usuario == null) {
    	            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
    	        }

    	        String tokenGenerado = UsuarioService.tokens.entrySet().stream()
    	                .filter(entry -> entry.getValue().equals(usuario))
    	                .map(Map.Entry::getKey)
    	                .findFirst()
    	                .orElseThrow(() -> new Exception("No se pudo generar el token."));

    	        return new ResponseEntity<>(tokenGenerado, HttpStatus.OK);
    	    } catch (Exception e) {
    	        return new ResponseEntity<>("Error interno al iniciar sesión", HttpStatus.INTERNAL_SERVER_ERROR);
    	    }
    	}


    /**
     * Convierte un Usuario en UsuarioDTO.
     *
     * @param usuario Usuario a convertir
     * @return UsuarioDTO convertido
     */
    private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
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
