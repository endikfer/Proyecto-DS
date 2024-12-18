/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.deusto.sd.auctions.dao.RetoRepository;
import es.deusto.sd.auctions.dao.UsuarioRepository;
import es.deusto.sd.auctions.dto.SesionDTO;
import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Login;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.TrainingSessionService;
import es.deusto.sd.auctions.service.UsuarioService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
    @Bean
    @Transactional
    CommandLineRunner initData(UsuarioService usuarioservice, TrainingSessionService sesionservice, UsuarioRepository usuariorepo, RetoRepository retorepo) {
		return args -> {
			retorepo.deleteAll();
	        usuariorepo.deleteAll();
	        
			Usuario Ana = new Usuario("Ana López", "contact@meta.com", Login.META, "2000-12-01", 62.0f, 165, 180, 55);
	        Usuario Maria = new Usuario("María Gómez", "help@meta.com", Login.META, "1993-10-10", 68.0f, 170, 185, 58);
	        usuariorepo.saveAll(List.of(Ana, Maria));
	        
			// Crear usuarios
			//usuarioservice.registro("Juan Pérez", "info@gmail.com", "1985-07-25", 70.5f, 175, 190, 60);
			//usuarioservice.registro("Carlos Díaz", "support@gmail.com", "1990-05-15", 80.0f, 180, 195, 65);
			
			//usuarioservice.logIn("contact@meta.com", "1a2b3c4d");
			//Thread.sleep(100);
			//usuarioservice.logIn("support@gmail.com", null);
			//Thread.sleep(100);
	       
			logger.info("Users saved!");			
			
			//Inicializacion de retos
			Reto r1 = new Reto("Reto 10K Running", Deporte.RUNNING, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0);
			Reto r2 = new Reto("Reto 50K Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(4), LocalDate.now().plusDays(20), 50, 0);
			Reto r3 = new Reto("Reto Maratón Running", Deporte.RUNNING, LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0);
			Reto r4 = new Reto("Reto 2 Horas Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(10), LocalDate.now().minusDays(4), 0, 120);
			Reto r5 = new Reto("Reto 30K Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0);
			Reto r6 = new Reto("Reto 1 Hora Running", Deporte.RUNNING, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60);
			Reto r7 = new Reto("Reto 20K Running", Deporte.RUNNING, LocalDate.now(), LocalDate.now().plusDays(15), 20, 0);
			
			retorepo.saveAll(List.of(r1, r2, r3, r4, r5, r6, r7));
			
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