package es.deusto.sd.auctions.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Reto {
	
	public String nombre;
	public String deporte;// ciclismo o running
	public LocalDate fecha_inicio;
	public LocalDate fecha_fin;
	public int distancia; //en km
	public int tiempo; // en minutos
	
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDeporte() {
		return deporte;
	}
	
	public void setDeporte(String deporte) {
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
	
	public Reto(String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, int distancia, int tiempo) {
		super();
		this.nombre = nombre;
		this.deporte = deporte;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.distancia = distancia;
		this.tiempo = tiempo;
	}
	
	@Override
	public String toString() {
		return "Reto [nombre=" + nombre + ", deporte=" + deporte + ", fecha_inicio=" + fecha_inicio + ", fecha_fin="
				+ fecha_fin + ", distancia=" + distancia + ", tiempo=" + tiempo + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(deporte, distancia, fecha_fin, fecha_inicio, nombre, tiempo);
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
		return Objects.equals(deporte, other.deporte) && distancia == other.distancia
				&& Objects.equals(fecha_fin, other.fecha_fin) && Objects.equals(fecha_inicio, other.fecha_inicio)
				&& Objects.equals(nombre, other.nombre) && tiempo == other.tiempo;
	}
}
