/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.deusto.sd.auctions.entity.Deporte;
import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.service.AuctionsService;
import es.deusto.sd.auctions.service.AuthService;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.TrainingSessionService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
    @Bean
    CommandLineRunner initData(AuctionsService auctionsService, AuthService authService, RetoService retoservice, TrainingSessionService sesionservice) {
		return args -> {			
			// Create some users
			User batman = new User("BruceWayne", "batman@dc.com", "Batm@n123!");
			User spiderman = new User("PeterParker", "spiderman@marvel.com", "Sp!derM4n2023");
			User superman = new User("ClarkKent", "superman@dc.com", "Sup3rm@n456!");
			User wonderWoman = new User("DianaPrince", "wonderwoman@dc.com", "Wond3rW0m@n!789");
			User captainMarvel = new User("CarolDanvers", "captainmarvel@marvel.com", "C@ptMarv3l#987");
			User blackWidow = new User("NatashaRomanoff", "blackwidow@marvel.com", "Bl@ckWid0w2023");

			authService.addUser(batman);
			authService.addUser(spiderman);
			authService.addUser(superman);
			authService.addUser(wonderWoman);
			authService.addUser(captainMarvel);
			authService.addUser(blackWidow);			
			
			logger.info("Users saved!");
			
			// Create some categories
			Category electronics = new Category("Electronics");
			Category sports = new Category("Sporting Goods");
			Category motors = new Category("Motors");
			
			auctionsService.addCategory(electronics);
			auctionsService.addCategory(sports);
			auctionsService.addCategory(motors);
			logger.info("Categories saved!");


			// Initialize auctions end date
			Calendar calendar = Calendar.getInstance();
			calendar.set(2024, Calendar.DECEMBER, 31);
			Date auctionEndDate = calendar.getTime();
			
			
			//Inicializacion de retos
			retoservice.crearReto(1L, "Reto 10K Running", "running", LocalDate.now().minusDays(5), LocalDate.now().plusDays(10), 10, 0);
			retoservice.crearReto(2L, "Reto 50K Ciclismo", "ciclismo", LocalDate.now(), LocalDate.now().plusDays(20), 50, 0);
			retoservice.crearReto(3L, "Reto Maratón Running", "running", LocalDate.now().minusDays(3), LocalDate.now().plusDays(15), 42, 0);
			retoservice.crearReto(4L, "Reto 2 Horas Ciclismo", "ciclismo", LocalDate.now().minusDays(10), LocalDate.now().plusDays(5), 0, 120);
			retoservice.crearReto(5L, "Reto 30K Ciclismo", "ciclismo", LocalDate.now().minusDays(7), LocalDate.now().plusDays(25), 30, 0);
			retoservice.crearReto(6L, "Reto 1 Hora Running", "running", LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 0, 60);
			retoservice.crearReto(7L, "Reto 20K Running", "running", LocalDate.now(), LocalDate.now().plusDays(15), 20, 0);
			
			// Inicialización de sesiones de entrenamiento
            trainingSessionService.createSession(new CrearSesionDTO("Morning Run", "running", 5.0, LocalDate.now().minusDays(2), 45));
            trainingSessionService.createSession(new CrearSesionDTO("Evening Ride", "ciclismo", 20.0, LocalDate.now().minusDays(1), 90));
            trainingSessionService.createSession(new CrearSesionDTO("Marathon Practice", "running", 15.0, LocalDate.now().minusDays(3), 120));
            trainingSessionService.createSession(new CrearSesionDTO("Cycling Challenge", "ciclismo", 40.0, LocalDate.now().minusDays(5), 180));
            trainingSessionService.createSession(new CrearSesionDTO("Weekend Walk", "running", 3.5, LocalDate.now().minusDays(7), 30));

            logger.info("Training sessions saved!");
            
			// Articles of Electronics category
            Article iphone = new Article(0, "Apple iPhone 14 Pro", 999.99f, auctionEndDate, electronics, batman);
            Article ps5 = new Article(1, "Sony PlayStation 5", 499.99f, auctionEndDate, electronics, spiderman);
            Article macbook = new Article(2, "MacBook Air M2", 1199.99f, auctionEndDate, electronics, wonderWoman);
            Article samsung = new Article(3, "Samsung Galaxy S21", 799.99f, auctionEndDate, electronics, captainMarvel);
            // Articles of Sporting Goods category
            Article tennisRacket = new Article(4, "Wilson Tennis Racket", 119.99f, auctionEndDate, sports, batman);
            Article soccerBall = new Article(5, "Adidas Soccer Ball", 29.99f, auctionEndDate, sports, blackWidow);
            Article fitbit = new Article(6, "Fitbit Charge 5 Fitness Tracker", 149.99f, auctionEndDate, sports, captainMarvel);
            Article peloton = new Article(7, "Peloton Exercise Bike", 1899.99f, auctionEndDate, sports, wonderWoman);
            // Articles of Motors category
            Article tesla = new Article(8, "Tesla Model 3", 42999.99f, auctionEndDate, motors, batman);
            Article civic = new Article(9, "Honda Civic 2021", 21999.99f, auctionEndDate, motors, superman);
            Article f150 = new Article(10, "Ford F-150 Pickup Truck", 33999.99f, auctionEndDate, motors, spiderman);
            Article corvette = new Article(11, "Chevrolet Corvette Stingray", 59999.99f, auctionEndDate, motors, captainMarvel);

            auctionsService.addArticle(iphone);
            auctionsService.addArticle(ps5);
            auctionsService.addArticle(macbook);
            auctionsService.addArticle(samsung);
            auctionsService.addArticle(tennisRacket);
            auctionsService.addArticle(soccerBall);
            auctionsService.addArticle(fitbit);
            auctionsService.addArticle(peloton);
            auctionsService.addArticle(tesla);
            auctionsService.addArticle(civic);
            auctionsService.addArticle(f150);
            auctionsService.addArticle(corvette);
            logger.info("Articles saved!");						
		};
	}
}