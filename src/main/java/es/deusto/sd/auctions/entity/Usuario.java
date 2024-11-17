package es.deusto.sd.auctions.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Usuario {
	private Long id;
    private String nombre;
    private Timestamp token;
    private String email;
    private Date fecha_nac;
    private float peso; //kilogramos
    private int altura; //centímetros
    private int frec_car_max; //en número de pulsaciones por minuto
    private int frec_car_rep; //en número de pulsaciones por minuto

    public Set<Reto> retosAceptados2 = new HashSet<>();
    private Set<Long> retosAceptados = new HashSet<>();
    
    //para crearlos a mano
	public Usuario(String nombre, String email, Date fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.fecha_nac = fecha_nac;
		this.peso = peso;
		this.altura = altura;
		this.frec_car_max = frec_car_max;
		this.frec_car_rep = frec_car_rep;
	}
	//para registro
	public Usuario(Long id, String nombre, String email, Date fecha_nac) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.fecha_nac = fecha_nac;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(Date fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}

	public int getFrec_car_max() {
		return frec_car_max;
	}

	public void setFrec_car_max(int frec_car_max) {
		this.frec_car_max = frec_car_max;
	}

	public int getFrec_car_rep() {
		return frec_car_rep;
	}

	public void setFrec_car_rep(int frec_car_rep) {
		this.frec_car_rep = frec_car_rep;
	}

	public Set<Long> getRetosAceptados() {
		return retosAceptados;
	}

	public void setRetosAceptados2(Set<Reto> retosAceptados2) {
		this.retosAceptados2 = retosAceptados2;
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
	
    public Set<Reto> getRetosAceptados2() {
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
