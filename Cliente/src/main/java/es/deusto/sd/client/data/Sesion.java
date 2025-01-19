package es.deusto.sd.client.data;

public record Sesion(
        Long id,
        String titulo,
<<<<<<< Updated upstream
        String deportename,
        Double distancia,
=======
        String deporte,
        double distancia,
>>>>>>> Stashed changes
        String fechaInicio,
        Integer duracion
) {
}