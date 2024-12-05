package es.deusto.ingenieria.sd.translation.server;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, String> {
    GoogleUser findByEmail(String email);
}
