package es.deusto.sd.strava.dto;

public class RetoDTO {
	
	public long id;
	public String nombre;
	public String deporte;  // ciclismo o running
	public String fecha_inicio;
	public String fecha_fin;
	public Integer distancia; //en km
	public Integer tiempo;  // en minutos

	// Constructor without parameters
	public RetoDTO() {
	}

	public RetoDTO(Long id, String nombre, String deporte, String fecha_inicio, String fecha_fin,
			Integer distancia, Integer tiempo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.deporte = deporte;
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
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

	public String getDeporte() {
		return deporte;
	}

	public void setDeporte(String deporte) {
		this.deporte = deporte;
	}

	public String getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public String getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(String fecha_fin) {
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

	@Override
	public String toString() {
		return "RetoDTO [id=" + id + ", nombre=" + nombre + ", deporte=" + deporte + ", fecha_inicio=" + fecha_inicio
				+ ", fecha_fin=" + fecha_fin + ", distancia=" + distancia + ", tiempo=" + tiempo + "]";
	}
	
	

}
