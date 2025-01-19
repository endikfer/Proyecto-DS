package es.deusto.sd.client.data;

public record Sesion(
        Long id,
        String titulo,
        String deportename,
        Double distancia,
        String deporte,
        String fechaInicio,
        Integer duracion
) {
}