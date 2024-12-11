package es.deusto.sd.auctions.external;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleServiceGateway implements ServiceGateway {

    private static final String GOOGLE_BASE_URL = "http://localhost:9000/google";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean verifyEmail(String email) {
        String url = GOOGLE_BASE_URL + "/verify?email=" + email;
        return restTemplate.getForObject(url, Boolean.class);
    }

    @Override
    public boolean validateLogin(String email, String password) {
        String url = GOOGLE_BASE_URL + "/validate?email=" + email + "&password=" + password;
        return restTemplate.postForObject(url, null, Boolean.class);
    }
}
