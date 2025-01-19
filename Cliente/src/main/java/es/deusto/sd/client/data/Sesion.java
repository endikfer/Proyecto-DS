package es.deusto.sd.client.data;

public record Sesion(
        Long id,
        String titulo,
        String deportename,
        double distancia,
        String fechaInicio,
        int duracion
) {
}