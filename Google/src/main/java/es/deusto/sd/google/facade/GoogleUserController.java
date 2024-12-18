package es.deusto.sd.google.facade;


import es.deusto.sd.google.service.GoogleUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google")
@Tag(name = "GoogleUser Controller", description = "Operaciones relacionadas con los usuarios de Google")
public class GoogleUserController {

    @Autowired
    private GoogleUserService service;

    @Operation(
        summary = "Verificar email",
        description = "Verifica si el email proporcionado existe",
        responses = {
            @ApiResponse(responseCode = "200", description = "Email verificado correctamente"),
            @ApiResponse(responseCode = "404", description = "Email no encontrado")
        }
    )
    @GetMapping("/verify")
    public boolean verifyEmail(@RequestParam("email") String email) {
        return service.verificarEmail(email);
    }

    @Operation(
        summary = "Validar login",
        description = "Valida el login del usuario con email y contraseña",
        responses = {
            @ApiResponse(responseCode = "200", description = "Login validado correctamente"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
        }
    )
    @GetMapping("/validate")
    public boolean validateLogin(
        @RequestParam("email") String email, 
        @RequestParam("contraseña") String password) {
        return service.validarLogin(email, password);
    }

}
