package es.deusto.sd.google;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// La anotación @SpringBootApplication habilita la configuración y el arranque de Spring Boot.
@SpringBootApplication
public class ServidorGoogle {

    public static void main(String[] args) {
        // Esta línea arranca la aplicación Spring Boot
        SpringApplication.run(ServidorGoogle.class, args);
    }
}
