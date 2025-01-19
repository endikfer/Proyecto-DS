package es.deusto.sd.client.data;

public record Sesion(
        Long id,
        String titulo,
        String deportename,
        Double distancia,
        String fechaInicio,
        Integer duracion
) {
}