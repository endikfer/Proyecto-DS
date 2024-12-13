package es.deusto.sd.auctions.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.auctions.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
