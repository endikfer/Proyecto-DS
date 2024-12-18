package es.deusto.sd.google.service;




import org.springframework.stereotype.Service;
import es.deusto.sd.google.dao.GoogleUserRepository;
import es.deusto.sd.google.entity.GoogleUser;

@Service
public class GoogleUserService {

    private GoogleUserRepository repository;
    
    public GoogleUserService(GoogleUserRepository repository) {
        this.repository = repository;
    }
    
    public boolean verificarEmail(String email) {
    	if (repository.findByEmail(email) == null) {
			return false;
		}
        return true;
    }

    public boolean validarLogin(String email, String password) {
        GoogleUser user = repository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
			return true;
		}
        return false;
    }
}
