
package es.deusto.sd.auctions.facade;

import es.deusto.sd.auctions.dto.UsuarioDTO;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // GET all users
    @GetMapping
    public ResponseEntity<?> getAllUsuarios() {
        return new ResponseEntity<>(usuarioService.getUsuarios(), HttpStatus.OK);
    }

    // GET a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
        if (usuario.isPresent()) {
            UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.get().getId(),
                                                    usuario.get().getNombre(),
                                                    usuario.get().getEmail(),
                                                    usuario.get().getFecha_nac(),
                                                    usuario.get().getPeso(),
                                                    usuario.get().getAltura(),
                                                    usuario.get().getFrec_car_max(),
                                                    usuario.get().getFrec_car_rep());
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // POST to register a new user
    @PostMapping("/registro")
    public ResponseEntity<?> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        // Llamar al método de registro en UsuarioService
        Usuario usuario = usuarioService.registro(
                usuarioDTO.getNombre(),
                usuarioDTO.getEmail(),
                usuarioDTO.getFecha_nac(),
                usuarioDTO.getPeso(),
                usuarioDTO.getAltura(),
                usuarioDTO.getFrec_car_max(),
                usuarioDTO.getFrec_car_rep()
        );

        if (usuario != null) {
            // Si el usuario se registra correctamente
            return new ResponseEntity<>("Usuario creado con éxito", HttpStatus.CREATED);
        } else {
            // Si el registro no es válido
            return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.BAD_REQUEST);
        }
    }

    // PUT to update an existing user (using 'registro' method for validation)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(id);
        if (usuarioOptional.isPresent()) {
            // Actualización de un usuario existente con el mismo método de 'registro'
            Usuario usuarioExistente = usuarioOptional.get();
            usuarioExistente.setNombre(usuarioDTO.getNombre());
            usuarioExistente.setEmail(usuarioDTO.getEmail());
            usuarioExistente.setFecha_nac(usuarioDTO.getFecha_nac());
            usuarioExistente.setPeso(usuarioDTO.getPeso());
            usuarioExistente.setAltura(usuarioDTO.getAltura());
            usuarioExistente.setFrec_car_max(usuarioDTO.getFrec_car_max());
            usuarioExistente.setFrec_car_rep(usuarioDTO.getFrec_car_rep());

            // No existe un método 'update' explícito, por lo que se puede considerar 'registro' como una forma de validación
            usuarioService.registro(
                    usuarioExistente.getNombre(),
                    usuarioExistente.getEmail(),
                    usuarioExistente.getFecha_nac(),
                    usuarioExistente.getPeso(),
                    usuarioExistente.getAltura(),
                    usuarioExistente.getFrec_car_max(),
                    usuarioExistente.getFrec_car_rep()
            );
            return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // DELETE a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(id);
        if (usuario.isPresent()) {
            // Para eliminar, podrías agregar un método de eliminación en UsuarioService.
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    // POST to log in a user (creating token)
    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody UsuarioDTO usuarioDTO) {
        String token = usuarioService.LogIn(usuarioDTO.getEmail(), usuarioDTO.getNombre());
        if (token != null) {
            return new ResponseEntity<>("Login exitoso, token: " + token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email o contraseña incorrectos", HttpStatus.UNAUTHORIZED);
        }
    }

    // POST to log out a user
    @PostMapping("/logout")
    public ResponseEntity<?> logOut(@RequestBody UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuario(usuarioDTO.getId());
        if (usuario.isPresent()) {
            usuarioService.LogOut(usuario.get());
            return new ResponseEntity<>("Logout exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
        }
    }
}
