package es.deusto.sd.google.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.google.entity.GoogleUser;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {
    GoogleUser findByEmail(String email);
}
