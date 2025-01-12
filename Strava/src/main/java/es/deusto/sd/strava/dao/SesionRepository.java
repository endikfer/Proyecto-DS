package es.deusto.sd.strava.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.deusto.sd.strava.entity.Sesion;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {

}
