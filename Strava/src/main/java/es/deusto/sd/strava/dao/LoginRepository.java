package es.deusto.sd.strava.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.strava.entity.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
	Optional<Login> findByName(String name);
}
