package es.deusto.sd.strava.dto;

import java.time.LocalDate;

public class SesionDTO {

    private Long id;// Solo necesario cuando se devuelve la sesión
    private String titulo;
    private String deporte;  // Tipo de deporte como String (se usa un enum en el backend)
    private double distancia; 
    private LocalDate fechaInicio;
    private int duracion;

    // Constructor para la creación de sesiones
    public SesionDTO(String titulo, String deporte, double distancia, LocalDate fechaInicio, int duracion) {
    	this.titulo = titulo;
        this.deporte = deporte;
        this.distancia = distancia;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
    }

    // Constructor para cuando ya existe una sesión (con ID)
    public SesionDTO(Long id, String titulo, String deporte, double distancia, LocalDate fechaInicio, int duracion) {
        this.id = id;
        this.titulo = titulo;
        this.deporte = deporte;
        this.distancia = distancia;
        this.fechaInicio = fechaInicio;
        this.duracion = duracion;
    }


	public SesionDTO() {
		// TODO Auto-generated constructor stub
	}

	// Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
