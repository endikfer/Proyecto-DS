package es.deusto.sd.client.data;

import java.time.LocalDate;

public record RetoAceptado(
		Long id,
		Long idUsu,
		String nombre,
		String deporte,
		LocalDate fecha_inicio,
		LocalDate fecha_fin,
		Integer distancia,
		Integer tiempo,
		Double progreso
	) {}
