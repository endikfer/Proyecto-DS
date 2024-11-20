package es.deusto.sd.auctions.dto;

import java.time.LocalDate;

public class RetoAcptadoDTO {

	public long id;
	public long idUsu;
	public String nombre;
	public String deporte;  // ciclismo o running
	public LocalDate fecha_inicio;
	public LocalDate fecha_fin;
	public Integer distancia; //en km
	public Integer tiempo;  // en minutos
	public Double progreso;
	
	
	public RetoAcptadoDTO(long id, long idUsu, String nombre, String deporte, LocalDate fecha_inicio,
			LocalDate fecha_fin, Integer distancia, Integer tiempo, Double progreso) {
		super();
		this.id = id;
		this.idUsu = idUsu;
		this.nombre = nombre;
		this.deporte = deporte;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.distancia = distancia;
		this.tiempo = tiempo;
		this.progreso = progreso;
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
	public Integer getDistancia() {
		return distancia;
	}
	public void setDistancia(Integer distancia) {
		this.distancia = distancia;
	}
	public Integer getTiempo() {
		return tiempo;
	}
	public void setTiempo(Integer tiempo) {
		this.tiempo = tiempo;
	}
	public Double getProgreso() {
		return progreso;
	}
	public void setProgreso(Double progreso) {
		this.progreso = progreso;
	}
	
	
}
