package es.deusto.sd.auctions.dto;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String fecha_nac;  // Cambiado a String
    private Float peso;
    private Integer altura;
    private Integer frec_car_max;
    private Integer frec_car_rep;

    // Constructor
    public UsuarioDTO(Long id, String nombre, String email, String fecha_nac, float peso, int altura, int frec_car_max, int frec_car_rep) {
    	this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fecha_nac = fecha_nac;  // Cambio aquí
        this.peso = peso;
        this.altura = altura;
        this.frec_car_max = frec_car_max;
        this.frec_car_rep = frec_car_rep;
    }

    // Getters y setters
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
}
