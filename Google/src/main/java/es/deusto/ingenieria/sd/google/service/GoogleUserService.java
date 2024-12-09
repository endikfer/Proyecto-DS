package es.deusto.ingenieria.sd.google.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.deusto.ingenieria.sd.google.dao.GoogleUserRepository;
import es.deusto.ingenieria.sd.google.entity.GoogleUser;

@Service
public class GoogleUserService {

    @Autowired
    private GoogleUserRepository repository;

    public boolean verifyEmail(String email) {
        return repository.findByEmail(email) != null;
    }

    public boolean validateLogin(String email, String password) {
        GoogleUser user = repository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
