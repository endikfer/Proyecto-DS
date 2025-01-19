package es.deusto.sd.client.data;

public record Reto(
		Long id,
		String nombre,
		String deporte,
		String fecha_inicio,
		String fecha_fin,
		Integer distancia,
		Integer tiempo
	) {}
