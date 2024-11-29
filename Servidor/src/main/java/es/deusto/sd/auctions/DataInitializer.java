/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.TrainingSessionService;
import es.deusto.sd.auctions.service.UsuarioService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
    @Bean
    CommandLineRunner initData(UsuarioService usuarioservice, RetoService retoservice, TrainingSessionService sesionservice) {
		return args -> {

			// Crear usuarios
			usuarioservice.registro("Juan Pérez", "info@google.com", "1985-07-25", 70.5f, 175, 190, 60);
			usuarioservice.registro("Ana López", "contact@meta.com", "2000-12-01", 62.0f, 165, 180, 55);
			usuarioservice.registro("Carlos Díaz", "support@google.com", "1990-05-15", 80.0f, 180, 195, 65);
			usuarioservice.registro("María Gómez", "help@meta.com", "1993-10-10", 68.0f, 170, 185, 58);
			
			usuarioservice.LogIn("info@google.com", null);
			Thread.sleep(100);
			usuarioservice.LogIn("contact@meta.com", null);
			Thread.sleep(100);
			usuarioservice.LogIn("support@google.com", null);
			Thread.sleep(100);
			usuarioservice.LogIn("help@meta.com", null);
	       
			logger.info("Users saved!");			
			
			//Inicializacion de retos
			retoservice.crearReto(1L, "Reto 10K Running", "running", LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0);
			retoservice.crearReto(2L, "Reto 50K Ciclismo", "ciclismo", LocalDate.now().minusDays(4), LocalDate.now().plusDays(20), 50, 0);
			retoservice.crearReto(3L, "Reto Maratón Running", "running", LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0);
			retoservice.crearReto(4L, "Reto 2 Horas Ciclismo", "ciclismo", LocalDate.now().minusDays(10), LocalDate.now().minusDays(4), 0, 120);
			retoservice.crearReto(5L, "Reto 30K Ciclismo", "ciclismo", LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0);
			retoservice.crearReto(6L, "Reto 1 Hora Running", "running", LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60);
			retoservice.crearReto(7L, "Reto 20K Running", "running", LocalDate.now(), LocalDate.now().plusDays(15), 20, 0);
			
			logger.info("Retos saved!");
			
			// Inicialización de sesiones de entrenamiento
			sesionservice.crearSesion(new SesionDTO("Morning Run", "running", 5.0, LocalDate.now().minusDays(2), 45));
			sesionservice.crearSesion(new SesionDTO("Evening Ride", "ciclismo", 20.0, LocalDate.now().minusDays(1), 90));
			sesionservice.crearSesion(new SesionDTO("Marathon Practice", "running", 1.0, LocalDate.now().minusDays(3), 120));
			sesionservice.crearSesion(new SesionDTO("Cycling Challenge", "ciclismo", 40.0, LocalDate.now().minusDays(5), 80));
			sesionservice.crearSesion(new SesionDTO("Weekend Walk", "running", 3.5, LocalDate.now().minusDays(7), 30));

            logger.info("Training sessions saved!");
            System.out.println(usuarioservice.obtenerTokens());
            	
		};
	}
}