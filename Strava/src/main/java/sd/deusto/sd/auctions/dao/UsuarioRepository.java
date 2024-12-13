package sd.deusto.sd.auctions.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.auctions.entity.Login;
import es.deusto.sd.auctions.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<String, Login> {
	Usuario findByEmail(String email);

}
