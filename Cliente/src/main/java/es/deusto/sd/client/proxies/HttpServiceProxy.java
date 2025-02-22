/**
 * This code is based on solutions provided by Claude Sonnet 3.5 and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.client.proxies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.deusto.sd.client.data.Reto;
import es.deusto.sd.client.data.RetoAceptado;
import es.deusto.sd.client.data.Sesion;
import es.deusto.sd.client.data.Usuario;

/**
 * HttpServiceProxy class is an implementation of the Service Proxy design
 * pattern that communicates with the AuctionsService using simple HTTP requests
 * via Java's HttpClient. This class serves as an intermediary for the client to
 * perform CRUD operations, such as user authentication (login/logout),
 * retrieving categories and articles, and placing bids on articles. By
 * encapsulating the HTTP request logic and handling various exceptions, this
 * proxy provides a cleaner interface for clients to interact with the
 * underlying service.
 * 
 * The class uses Java's HttpClient which allows for asynchronous and
 * synchronous communication with HTTP servers. It leverages the `HttpRequest`
 * and `HttpResponse` classes to construct and send requests, simplifying the
 * process of making HTTP calls. The ObjectMapper from the Jackson library is
 * employed to serialize and deserialize JSON data, facilitating easy conversion
 * between Java objects and their JSON representations. This is particularly
 * useful for converting complex data structures, like the `Credentials`,
 * `Category`, and `Article` classes, into JSON format for transmission in HTTP
 * requests, and vice versa for processing the responses.
 * 
 * The absence of the @Service annotation indicates that this class is not
 * managed by a Spring container, which means that it will not benefit from
 * Spring's dependency injection features. Instead, it operates independently,
 * which can be suitable for applications preferring a more lightweight approach
 * without the overhead of a full Spring context.
 * 
 * (Description generated with ChatGPT 4o mini)
 */
public class HttpServiceProxy implements IStravaServiceProxy {
	private static final String BASE_URL = "http://localhost:8080";
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	public HttpServiceProxy() {
		this.httpClient = HttpClient.newHttpClient();
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public void crearReto(String nombre, String deporte, String fecha_inicio, String fecha_fin, Integer distancia,
			Integer tiempo, String token) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(BASE_URL + "/acciones/retos/crear?nombre=" + nombre + "&deporte=" + deporte
							+ "&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&distancia=" + distancia
							+ "&tiempo=" + tiempo))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(token))
					.build();

			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

			switch (response.statusCode()) {
			case 200 -> {
			} // Reto creado correctamente
			case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
			case 500 -> throw new RuntimeException("Internal server error");
			default ->
				throw new RuntimeException("Fallo al crear el reto con el codigo de estatus: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error creando el reto.", e);
		}
	}

	@Override
	public List<Reto> consultarReto(String deporte, String fecha, String token) {
		try {
			// Construir la URL base
			StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/acciones/retos");

			// Si 'deporte' no es null, añadirlo a la URL
			if (deporte != null) {
				urlBuilder.append("?deporte=").append(deporte);
			}

			// Si 'fecha' no es null, añadirlo a la URL
			if (fecha != null) {
				// Si ya se añadió un parámetro, agregamos el '&'
				if (deporte != null) {
					urlBuilder.append("&");
				} else {
					// Si no se añadió 'deporte', no necesitamos '&'
					urlBuilder.append("?");
				}
				urlBuilder.append("fecha=").append(fecha);
			}

			// Construir la solicitud
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlBuilder.toString()))
					.header("Content-Type", "application/json").header("token", token).GET().build();

			// Enviar la solicitud y obtener la respuesta
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Procesar la respuesta
			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Reto.class));
			case 204 -> throw new RuntimeException("No Content: No hay retos aceptados");
			case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
			case 500 -> throw new RuntimeException("Internal server error");
			default -> throw new RuntimeException(
					"Fallo al obtener los reto con el codigo de estatus: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error obteniendo el reto.", e);
		}
	}

	@Override
	public void aceptarReto(Long retoId, String token) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
		            .uri(URI.create(BASE_URL + "/acciones/retos/aceptar?retoId=" + retoId))
		            .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(token))
					.build();

		        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
		        

		        switch (response.statusCode()) {
		            case 200 -> System.out.println("Reto aceptado correctamente" + retoId);
		            case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
		            case 409 -> throw new RuntimeException("Conflict: El reto ya fue aceptado previamente");
		            case 500 -> throw new RuntimeException("Internal server error");
		            default -> throw new RuntimeException("Fallo al aceptar el reto con el código de estado: " + response.statusCode());
		        }
		    } catch (IOException | InterruptedException e) {
		        throw new RuntimeException("Error aceptando el reto.", e);
		    }
	}

	@Override
	public List<RetoAceptado> consultarRetosAceptados(String token) {

		try {
			// Construir la URL base
			StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/acciones/retos/aceptados");


			// Construir la solicitud
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlBuilder.toString()))
					.header("Content-Type", "application/json").header("token", token).GET().build();

			// Enviar la solicitud y obtener la respuesta
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			// Procesar la respuesta
			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, RetoAceptado.class));
			case 204 -> throw new RuntimeException("No Content: No hay retos aceptados para el usuario");
			case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
			case 500 -> throw new RuntimeException("Internal server error");
			default -> throw new RuntimeException(
					"Fallo al obtener los reto con el codigo de estatus: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error obteniendo el reto.", e);
		}

	}

	@Override
	public void registro(String nombre, String email, String tipo, String fecha_nac, float peso, int altura,
			int frec_car_max, int frec_car_rep) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(BASE_URL + "/usuarios/registro?nombre=" + nombre + "&email=" + email + "&tipo="
							+ tipo + "&fecha_nac=" + fecha_nac + "&peso=" + peso + "&altura=" + altura
							+ "&frec_car_max=" + frec_car_max + "&frec_car_rep=" + frec_car_rep))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

			switch (response.statusCode()) {
			case 201 -> {
			} // Usuario registrado correctamente
			case 401 -> throw new RuntimeException("Bad Request: Datos inválidos o incompletos");
			case 500 -> throw new RuntimeException("Internal server error");
			default ->
				throw new RuntimeException("Fallo al crear usuario con el código de estado: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error registrando usuario.", e);
		}
	}

	@Override
	public String logIn(String email, String contrasenia) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(BASE_URL + "/usuarios/login?email=" + email + "&contraseña=" + contrasenia))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return switch (response.statusCode()) {
			case 200 -> response.body();
			case 400 -> throw new RuntimeException("Bad Request: Usuario no registrado o datos incorrectos.");
			case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials, login failed");
			case 500 -> throw new RuntimeException("Internal Server Error: Error interno en el servidor.");
			default ->
				throw new RuntimeException("Fallo al hacer login con el código de estado: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error registrando usuario.", e);
		}

	}

	@Override
	public void logOut(String token) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/usuarios/logout"))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(token))
					.build();

			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

			switch (response.statusCode()) {
			case 200 -> {
			} // Sesión cerrada correctamente
			case 400 -> throw new RuntimeException("Bad Request: Token no válido.");
			case 500 -> throw new RuntimeException("Internal Server Error: Error interno en el servidor.");
			default ->
				throw new RuntimeException("Fallo al cerrar sesión con el código de estado: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error cerrando sesión.", e);
		}
	}

	@Override
	public List<Sesion> getSesionesRecientes() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/sesiones/recientes"))
	                .header("Content-Type", "application/json")
	                .GET()
	                .build();
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Sesion.class));
			case 204 -> throw new RuntimeException("No Content: No existen sesiones");
			case 500 -> throw new RuntimeException("Internal server error");
			default ->
				throw new RuntimeException("Fallo al acceder a las sesiones recientes: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("Error accediendo a las sesiones recientes.", e);
		}
	}

	@Override
	public List<Sesion> getSesionesPorFecha(String startDate, String endDate) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(
							BASE_URL + "/sesiones/sesionesporfecha?startDate=" + startDate + "&endDate=" + endDate))
					.header("Content-Type", "application/json").GET().build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Sesion.class));
			case 204 -> throw new RuntimeException("No Content: No existen sesiones");
			case 400 -> throw new RuntimeException("Formato de fecha invalido");
			case 500 -> throw new RuntimeException("Internal server error");
			default ->
				throw new RuntimeException("Fallo al acceder a las sesiones recientes: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error accediendo a las sesiones recientes.", e);
		}
	}

	@Override
	public void crearSesion(String titulo, String deporte, double distancia, String fechaInicio,
			int duracion) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(BASE_URL + "/sesiones/crear?titulo=" + titulo
							+ "&deporte=" + deporte + "&distancia=" + distancia + "&fechaInicio=" + fechaInicio
							+ "&duracion=" + duracion))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.noBody()).build();
			HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
			switch (response.statusCode()) {
			case 200 -> {
			} // Sesión creada exitosamente
			case 400 -> throw new RuntimeException("Bad Request: Datos inválidos o faltantes en la solicitud");
			case 500 -> throw new RuntimeException("Internal Server Error: Error en el servidor al crear la sesión");
			default -> throw new RuntimeException("Error inesperado con código de estado: " + response.statusCode());
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error al intentar crear la sesión", e);
		}
	}

	@Override
	public List<Usuario> obtenerTodosLosUsuarios() {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/usuarios"))
					.header("Content-Type", "application/json").GET().build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
			case 204 -> throw new RuntimeException("No Content: No hay usuarios registrados");
			case 500 -> throw new RuntimeException("Internal server error");
			default -> throw new RuntimeException(
					"Fallo al consultar los usuarios registrados con el código de estado: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error al consultar los usuarios registrados.", e);
		}
	}

	@Override
	public Map<String, Usuario> obtenerTokens() {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/usuarios/tokens"))
					.header("Content-Type", "application/json").GET().build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			return switch (response.statusCode()) {
			case 200 -> objectMapper.readValue(response.body(),
					objectMapper.getTypeFactory().constructMapLikeType(Map.class, String.class, Usuario.class));
			case 500 -> throw new RuntimeException("Internal server error: Error interno en el servidor.");
			default -> throw new RuntimeException(
					"Fallo al consultar los retos aceptados con el código de estado: " + response.statusCode());
			};
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error consultando los tokens.", e);
		}
	}

}