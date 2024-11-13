package es.deusto.sd.auctions.entity;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
	private Long id;
    private String nombre;
    private Set<Long> retosAceptados = new HashSet<>();
	public Usuario(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
    
	
	public void aceptarReto(Long retoId) {
        retosAceptados.add(retoId);
    }
}
