package es.deusto.sd.ingenieria.client.data;
import es.deusto.sd.auctions.entity.Login;

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
