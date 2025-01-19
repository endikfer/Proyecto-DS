package es.deusto.sd.strava.dto;

import java.time.LocalDate;
import java.util.Objects;

public class RetoAcptadoDTO {

	public long id;
	public long idUsu;
	public String nombre;
	public String deporte;  // ciclismo o running
	public String fecha_inicio;
	public String fecha_fin;
	public Integer distancia; //en km
	public Integer tiempo;  // en minutos
	public Double progreso;
	
	
	public RetoAcptadoDTO(long id, long idUsu, String nombre, String deporte, String fecha_inicio,
			String fecha_fin, Integer distancia, Integer tiempo, Double progreso) {
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
	
	
	public RetoAcptadoDTO(RetoAcptadoDTO r) {
		// TODO Auto-generated constructor stub
		this.id = r.getId();
		this.idUsu = r.getIdUsu();
		this.nombre = r.getNombre();
		this.deporte = r.getDeporte();
		this.fecha_inicio = r.getFecha_inicio();
		this.fecha_fin = r.getFecha_fin();
		this.distancia = r.getDistancia();
		this.tiempo = r.getTiempo();
		this.progreso = r.getProgreso();
	}


	public long getIdUsu() {
		return idUsu;
	}


	public void setIdUsu(long idUsu) {
		this.idUsu = idUsu;
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
	public Double getProgreso() {
		return progreso;
	}
	public void setProgreso(Double progreso) {
		this.progreso = progreso;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetoAcptadoDTO that = (RetoAcptadoDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(idUsu, that.idUsu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsu);
    }
	
}
