package es.deusto.sd.google;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import es.deusto.sd.google.dao.GoogleUserRepository;
import es.deusto.sd.google.entity.GoogleUser;
import es.deusto.sd.google.service.GoogleUserService;

@Configuration
public class GoogleInitializer {
	private static final Logger logger = LoggerFactory.getLogger(GoogleInitializer.class);

	
    @Bean
    @Transactional
    CommandLineRunner initData(GoogleUserService googleUserService, GoogleUserRepository googleUserRepository) {
        return args -> {
            googleUserRepository.deleteAll();

            GoogleUser user1 = new GoogleUser(null, "info@gmail.com", "123");
            GoogleUser user2 = new GoogleUser(null, "support@gmail.com", "456");

            googleUserRepository.saveAll(List.of(user1, user2));

            logger.info("Google Users saved!");
        };
    }
}

