/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.strava;

import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import es.deusto.sd.strava.dao.RetoRepository;
import es.deusto.sd.strava.dao.SesionRepository;
import es.deusto.sd.strava.dao.UsuarioRepository;
import es.deusto.sd.strava.entity.Deporte;
import es.deusto.sd.strava.entity.Login;
import es.deusto.sd.strava.entity.Reto;
import es.deusto.sd.strava.entity.Sesion;
import es.deusto.sd.strava.entity.Usuario;
import es.deusto.sd.strava.service.UsuarioService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
    @Bean
    @Transactional
    CommandLineRunner initData(UsuarioService usuarioservice, UsuarioRepository usuariorepo, RetoRepository retorepo, SesionRepository sesionrepo) {
		return args -> {
			retorepo.deleteAll();
	        usuariorepo.deleteAll();
	        sesionrepo.deleteAll();
	        
			//Inicializacion de usuarios
	        Usuario Ana = new Usuario("Ana López", "contact@meta.com", Login.META, "2000-12-01", 62.0f, 165, 180, 55);
	        Usuario Maria = new Usuario("María Gómez", "help@meta.com", Login.META, "1993-10-10", 68.0f, 170, 185, 58);
	        Usuario Juan = new Usuario("Juan Pérez", "info@gmail.com", Login.GOOGLE, "1985-07-25", 70.5f, 175, 190, 60);
	        Usuario Carlos = new Usuario("Carlos Díaz", "support@gmail.com", Login.GOOGLE, "1990-05-15", 80.0f, 180, 195, 65);

	        usuariorepo.saveAll(List.of(Ana, Maria, Juan, Carlos));

			logger.info("Users saved!");			
			
			//Inicializacion de retos
			Reto r1 = new Reto("Reto 10K Running", Deporte.RUNNING, LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0);
			Reto r2 = new Reto("Reto 50K Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(4), LocalDate.now().plusDays(20), 50, 0);
			Reto r3 = new Reto("Reto Maratón Running", Deporte.RUNNING, LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0);
			Reto r4 = new Reto("Reto 2 Horas Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(10), LocalDate.now().minusDays(4), 0, 120);
			Reto r5 = new Reto("Reto 30K Ciclismo", Deporte.CICLISMO, LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0);
			Reto r6 = new Reto("Reto 1 Hora Running", Deporte.RUNNING, LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60);
			Reto r7 = new Reto("Reto 20K Running", Deporte.RUNNING, LocalDate.now(), LocalDate.now().plusDays(15), 20, 0);
			
			List<Reto> retos = retorepo.saveAll(List.of(r1, r2, r3, r4, r5, r6, r7));
			retos.forEach(reto -> System.out.println("ID generado: " + reto.getId()));
			
			logger.info("Retos saved!");
			
			// Inicialización de sesiones de entrenamiento
			Sesion s1 = new Sesion( "Morning Run", Deporte.RUNNING, 5.0, LocalDate.now().minusDays(2), 45);
			Sesion s2 = new Sesion( "Evening Ride", Deporte.CICLISMO, 20.0, LocalDate.now().minusDays(1), 90);
			Sesion s3 = new Sesion( "Marathon Practice", Deporte.RUNNING, 1.0, LocalDate.now().minusDays(3), 120);
			Sesion s4 = new Sesion( "Cycling Challenge", Deporte.CICLISMO, 40.0, LocalDate.now().minusDays(5), 80);
			Sesion s5 = new Sesion( "Weekend Walk", Deporte.RUNNING, 3.5, LocalDate.now().minusDays(7), 30);
			Sesion s6 = new Sesion( "Mountain route", Deporte.RUNNING, 1.5, LocalDate.now().minusDays(5), 30);
			Sesion s7 = new Sesion( "Cycling", Deporte.CICLISMO, 3.3, LocalDate.now().minusDays(3), 30);

			sesionrepo.saveAll(List.of(s1, s2, s3, s4, s5,s6,s7));

            logger.info("Training sessions saved!");
            	
		};
	}
}