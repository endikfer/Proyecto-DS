package es.deusto.sd.strava.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class GoogleServiceGateway implements ServiceGateway {

    private static final String GOOGLE_BASE_URL = "http://localhost:9000/google/";
    private final RestTemplate restTemplate = new RestTemplate();
	private static final Logger logger = LoggerFactory.getLogger(GoogleServiceGateway.class);
    @Override
    public boolean verifyEmail(String email) {
        String url = GOOGLE_BASE_URL + "verify?email=" + email;
        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
			logger.error("No se encuentra la url: "+ url);	
            return false;
        }
    }

    @Override
    public boolean validateLogin(String email, String password) {
        String url = GOOGLE_BASE_URL + "validate?email=" + email + "&contraseña=" + password;
        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
			logger.error("No se encuentra la url: "+ url);	
            return false;
        }
    }
    
}
