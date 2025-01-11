package es.deusto.sd.ingenieria.client.data;
import java.time.LocalDate;
import es.deusto.sd.auctions.entity.Deporte;

public record Sesion(
        Long id,
        Long usuarioId,
        String titulo,
        Deporte deporte,
        double distancia,
        LocalDate fechaInicio,
        int duracion,
        int tiempo
) {
}