package es.deusto.sd.client.data;
import java.time.LocalDate;

public record Sesion(
        Long id,
        String titulo,
        String deportename,
        double distancia,
        LocalDate fechaInicio,
        int duracion
) {
}