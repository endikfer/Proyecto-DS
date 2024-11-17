package es.deusto.sd.auctions.facade;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PathVariable;

import es.deusto.sd.auctions.entity.Reto;
import es.deusto.sd.auctions.entity.Sesion;
import es.deusto.sd.auctions.entity.Usuario;
import es.deusto.sd.auctions.service.RetoService;
import es.deusto.sd.auctions.service.UsuarioService;

public class UsuarioController {
	
	UsuarioService US;

	public UsuarioController(UsuarioService US) {
		this.US = US;
	}

}
