package es.deusto.ingenieria.sd.google.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import es.deusto.ingenieria.sd.google.service.GoogleUserService;

@RestController
@RequestMapping("/google")
public class GoogleUserController {

    @Autowired
    private GoogleUserService service;

    @GetMapping("/verify/{email}")
    public boolean verifyEmail(@PathVariable String email) {
        return service.verifyEmail(email);
    }

    @PostMapping("/validate")
    public boolean validateLogin(@RequestParam String email, @RequestParam String password) {
        return service.validateLogin(email, password);
    }
}
