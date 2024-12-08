package es.deusto.ingenieria.sd.google.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.ingenieria.sd.google.entity.GoogleUser;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
    GoogleUser findByEmail(String email);
}
