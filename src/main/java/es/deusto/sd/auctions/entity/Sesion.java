package es.deusto.sd.auctions.entity;

import java.time.LocalDate;

public class Sesion {

	private Long id; // ID único de la sesión
    private Long usuarioId; // ID del usuario que realizó la sesión
    private String titulo; // Título de la sesión
    private String deporte; // Tipo de deporte: "ciclismo" o "running"
    private double distancia; // Distancia en kilómetros
    private LocalDate fechaInicio; // Fecha y hora de inicio
    private int duracion; // Duración en minutos
    private int tiempo;

    public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	// Constructor
    public void SesionEntrenamiento(Long id, Long usuarioId, String titulo, String deporte, double distancia, LocalDate fechaInicio, int duracion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.deporte = deporte;
        this.distancia = distancia;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
