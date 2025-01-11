package es.deusto.sd.client.data;

public record Usuario(
    Long id,
    String nombre,
    String email,
    Login tipo,
    String fechaNac,
    Float peso, // kilogramos
    Integer altura, // centímetros
    Integer frecCarMax, // en número de pulsaciones por minuto
    Integer frecCarRep // en número de pulsaciones por minuto
) {}
