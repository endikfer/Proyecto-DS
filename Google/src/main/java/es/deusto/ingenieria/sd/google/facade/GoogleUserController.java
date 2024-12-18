package es.deusto.ingenieria.sd.google.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import es.deusto.ingenieria.sd.google.service.GoogleUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/google")
@Tag(name = "Google User Controller", description = "Operaciones relacionadas con los usuarios de Google")
public class GoogleUserController {

    @Autowired
    private GoogleUserService service;

    @Operation(
        summary = "Verificar email",
        description = "Verifica si un email existe en la base de datos de Google",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: El email existe"),
            @ApiResponse(responseCode = "404", description = "Not Found: El email no existe"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @GetMapping("/verify")
    public boolean verifyEmail(
        @Parameter(name = "email", description = "Email del usuario a verificar", required = true, example = "usuario@gmail.com")
        @RequestParam("email") String email) {
        return service.verificarEmail(email);
    }

    @Operation(
        summary = "Validar login",
        description = "Valida las credenciales del usuario (email y contraseña)",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK: Login válido"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Email o contraseña incorrectos"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @PostMapping("/validate")
    public boolean validateLogin(
        @Parameter(name = "email", description = "Email del usuario", required = true, example = "usuario@gmail.com")
        @RequestParam("email") String email,

        @Parameter(name = "password", description = "Contraseña del usuario", required = true, example = "password123")
        @RequestParam("password") String password) {

        return service.validarLogin(email, password);
    }
}
