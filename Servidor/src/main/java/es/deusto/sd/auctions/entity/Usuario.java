package es.deusto.sd.auctions.entity;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
	private Long id;
    private String nombre;
    private String email;
    private String fecha_nac;
    private Float peso; //kilogramos
    private Integer altura; //centímetros
    private Integer frec_car_max; //en número de pulsaciones por minuto
    private Integer frec_car_rep; //en número de pulsaciones por minuto

    public Set<Reto> retosAceptados2 = new HashSet<>();
    private Set<Long> retosAceptados = new HashSet<>();
    //para crearlos a mano
	public Usuario(String nombre, String email, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
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
	public Usuario(Long id, String nombre, String email, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.fecha_nac = fecha_nac;
		this.peso = peso;
		this.altura = altura;
		this.frec_car_max = frec_car_max;
		this.frec_car_rep = frec_car_rep;
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

	public String getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(String fecha_nac) {
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
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", email=" + email + ", fecha_nac="
				+ fecha_nac + ", peso=" + peso + ", altura=" + altura + ", frec_car_max=" + frec_car_max
				+ ", frec_car_rep=" + frec_car_rep + ", retosAceptados2=" + retosAceptados2 + ", retosAceptados="
				+ retosAceptados + "]";
	}
	
	
}
