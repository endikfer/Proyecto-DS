package es.deusto.sd.client.data;

public record RetoAceptado(
		Long id,
		Long idUsu,
		String nombre,
		String deporte,
		String fecha_inicio,
		String fecha_fin,
		Integer distancia,
		Integer tiempo,
		Double progreso
	) {}
