package es.deusto.sd.strava.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "retos")
public class Reto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
	
	@Column(nullable = false)
	public String nombre;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public Deporte deporte;  // ciclismo o running
	
	@Column(nullable = false)
	public LocalDate fecha_inicio;
	
	@Column(nullable = false)
	public LocalDate fecha_fin;
	
	@Column(nullable = false)
	public Integer distancia; //en km
	
	@Column(nullable = false)
	public Integer tiempo; // en minutos
	
	public Reto() {
	}

	public Reto(String nombre, Deporte deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {
		this.nombre = nombre;
		this.deporte = deporte;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		
		if ((distancia == 0 && tiempo == 0) || (distancia != 0 && tiempo != 0)) {
            throw new IllegalArgumentException("El reto debe tener un objetivo de distancia o de tiempo, pero no ambos.");
        }
		
		this.distancia = distancia;
		this.tiempo = tiempo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Deporte getDeporte() {
		return deporte;
	}
	
	public void setDeporte(Deporte deporte) {
		this.deporte = deporte;
	}
	
	public LocalDate getFecha_inicio() {
		return fecha_inicio;
	}
	
	public void setFecha_inicio(LocalDate fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	
	public LocalDate getFecha_fin() {
		return fecha_fin;
	}
	
	public void setFecha_fin(LocalDate fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	
	public int getDistancia() {
		return distancia;
	}
	
	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}
	
	public int getTiempo() {
		return tiempo;
	}
	
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	@Override
	public String toString() {
		return "Reto [id=" + id + ", nombre=" + nombre + ", deporte=" + deporte + ", fecha_inicio=" + fecha_inicio
				+ ", fecha_fin=" + fecha_fin + ", distancia=" + distancia + ", tiempo=" + tiempo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reto other = (Reto) obj;
		return id == other.id;
	}
	
	
}
