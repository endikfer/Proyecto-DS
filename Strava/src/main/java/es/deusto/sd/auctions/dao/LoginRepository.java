package es.deusto.sd.auctions.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import es.deusto.sd.auctions.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	Optional<Login> findByName(String name);
}
