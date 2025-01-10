package es.deusto.sd.auctions.client.data;

import java.time.LocalDate;

public record Reto(
		Long id,
		String nombre,
		String deporte,
		LocalDate fecha_inicio,
		LocalDate fecha_fin,
		Integer distancia,
		Integer tiempo
	) {}
