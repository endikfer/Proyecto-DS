package es.deusto.sd.auctions.dto;

public class ProgresoRetoDTO {
	private Long retoId;
    private String nombre;
    private String deporte;
    private double progreso;

    public ProgresoRetoDTO(Long retoId, String nombre, String deporte, double progreso) {
        this.retoId = retoId;
        this.nombre = nombre;
        this.deporte = deporte;
        this.progreso = progreso;
    }

    // Getters y Setters
    public Long getRetoId() {
        return retoId;
    }

    public void setRetoId(Long retoId) {
        this.retoId = retoId;
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

    public double getProgreso() {
        return progreso;
    }

    public void setProgreso(double progreso) {
        this.progreso = progreso;
    }
    
    
}
