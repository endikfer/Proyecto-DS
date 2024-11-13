package es.deusto.sd.auctions.entity;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
	private Long id;
    private String nombre;
    private String token;
    public Set<Reto> retosAceptados2 = new HashSet<>();
    private Set<Long> retosAceptados = new HashSet<>();
	public Usuario(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
    
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public Set<Reto> getRetosAceptados2() {
		return retosAceptados2;
	}


	public void setRetosAceptados2(Set<Reto> retosAceptados2) {
		this.retosAceptados2 = retosAceptados2;
	}


	public Set<Long> getRetosAceptados() {
		return retosAceptados;
	}


	public void setRetosAceptados(Set<Long> retosAceptados) {
		this.retosAceptados = retosAceptados;
	}


	public void aceptarReto(Long retoId) {
        retosAceptados.add(retoId);
    }
	
	public void añadirReto(Reto reto) {
		retosAceptados2.add(reto);
    }
	
    public Set<Reto> getRetosAceptados() {
        return retosAceptados2;
    }
	
	public void mostrarRetos() {
        if (retosAceptados.isEmpty()) {
            System.out.println("No hay retos aceptados.");
        } else {
            System.out.println("Retos aceptados:");
            for (Reto reto : retosAceptados2) {
                System.out.println(reto);
            }
        }
    }
	
	public boolean estaAutenticado() {
        return token != null;
    }
	
}
