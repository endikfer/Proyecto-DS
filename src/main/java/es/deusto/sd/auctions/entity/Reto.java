package es.deusto.sd.auctions.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Reto {
	
	public long id;
	public String nombre;
	public Deporte deporte;  // ciclismo o running
	public LocalDate fecha_inicio;
	public LocalDate fecha_fin;
	public Integer distancia; //en km
	public Integer tiempo; // en minutos
	
	
	public Reto(long id, String nombre, Deporte deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.deporte = deporte;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		
		if ((distancia == null && tiempo == null) || (distancia != null && tiempo != null)) {
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
