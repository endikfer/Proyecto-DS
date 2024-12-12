package es.deusto.sd.auctions.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.auctions.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByEmail(String email);
}