/**
 * This code is based on solutions provided by Claude Sonnet 3.5 and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.client.proxies;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.deusto.sd.client.data.Sesion;
import es.deusto.sd.client.data.Article;
import es.deusto.sd.client.data.Category;
import es.deusto.sd.client.data.Credentials;
import es.deusto.sd.client.data.Login;
import es.deusto.sd.client.data.Reto;
import es.deusto.sd.client.data.Usuario;

/**
 * HttpServiceProxy class is an implementation of the Service Proxy design pattern
 * that communicates with the AuctionsService using simple HTTP requests via Java's
 * HttpClient. This class serves as an intermediary for the client to perform 
 * CRUD operations, such as user authentication (login/logout), retrieving categories 
 * and articles, and placing bids on articles. By encapsulating the HTTP request logic 
 * and handling various exceptions, this proxy provides a cleaner interface for clients 
 * to interact with the underlying service.
 * 
 * The class uses Java's HttpClient which allows for asynchronous and synchronous 
 * communication with HTTP servers. It leverages the `HttpRequest` and `HttpResponse` 
 * classes to construct and send requests, simplifying the process of making HTTP calls. 
 * The ObjectMapper from the Jackson library is employed to serialize and deserialize 
 * JSON data, facilitating easy conversion between Java objects and their JSON 
 * representations. This is particularly useful for converting complex data structures, 
 * like the `Credentials`, `Category`, and `Article` classes, into JSON format for 
 * transmission in HTTP requests, and vice versa for processing the responses.
 * 
 * The absence of the @Service annotation indicates that this class is not managed 
 * by a Spring container, which means that it will not benefit from Spring's 
 * dependency injection features. Instead, it operates independently, which can 
 * be suitable for applications preferring a more lightweight approach without 
 * the overhead of a full Spring context.
 * 
 * (Description generated with ChatGPT 4o mini)
 */
public class HttpServiceProxy implements IServiceProxy {
    private static final String BASE_URL = "http://localhost:8081";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpServiceProxy() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String login(Credentials credentials) {
        try {
            String credentialsJson = objectMapper.writeValueAsString(credentials);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credentialsJson))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Login failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    @Override
    public void logout(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/logout"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            switch (response.statusCode()) {
                case 204 -> {} // Logout successful
                case 401 -> throw new RuntimeException("Unauthorized: Invalid token, logout failed");
                default -> throw new RuntimeException("Logout failed with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during logout", e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auctions/categories"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Category.class));
                case 204 -> throw new RuntimeException("No Content: No categories found");
                case 500 -> throw new RuntimeException("Internal server error while fetching categories");
                default -> throw new RuntimeException("Failed to fetch categories with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while fetching categories", e);
        }
    }
    
    @Override
    public List<Article> getArticlesByCategory(String categoryName, String currency) {
        try {
            // Encode the category name to handle spaces and special characters
            String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
        	
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auctions/categories/" + encodedCategoryName + "/articles?currency=" + currency))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Article.class));
                case 204 -> throw new RuntimeException("No Content: Category has no articles");
                case 400 -> throw new RuntimeException("Bad Request: Currency not supported");
                case 404 -> throw new RuntimeException("Not Found: Category not found");
                case 500 -> throw new RuntimeException("Internal server error while fetching articles");
                default -> throw new RuntimeException("Failed to fetch articles with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while fetching articles by category", e);
        }
    }

    @Override
    public Article getArticleDetails(Long articleId, String currency) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auctions/articles/" + articleId + "/details?currency=" + currency))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> objectMapper.readValue(response.body(), Article.class);
                case 400 -> throw new RuntimeException("Bad Request: Currency not supported");
                case 404 -> throw new RuntimeException("Not Found: Article not found");
                case 500 -> throw new RuntimeException("Internal server error while fetching article details");
                default -> throw new RuntimeException("Failed to fetch article details with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while fetching article details", e);
        }
    }

    @Override
    public void makeBid(Long articleId, Float amount, String currency, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auctions/articles/" + articleId + "/bid?amount=" + amount + "&currency=" + currency))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            switch (response.statusCode()) {
                case 204 -> {} // Bid placed successfully
                case 400 -> throw new RuntimeException("Bad Request: Currency not supported");
                case 401 -> throw new RuntimeException("Unauthorized: User not authenticated");
                case 404 -> throw new RuntimeException("Not Found: Article not found");
                case 409 -> throw new RuntimeException("Conflict: Bid amount must be greater than the current price");
                case 500 -> throw new RuntimeException("Internal server error while placing a bid");
                default -> throw new RuntimeException("Failed to make a bid with status code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while making a bid", e);
        }
    }

	@Override
	public void crearReto(String nombre, String deporte, LocalDate fecha_inicio, LocalDate fecha_fin, Integer distancia,
			Integer tiempo, String token) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/acciones/retos/crear?nombre=" + nombre + "&deporte=" + 
	                		deporte + "&fecha_inicio=" + fecha_inicio + "&fecha_fin=" + fecha_fin + "&distancia=" +
	                		distancia + "&tiempo=" + tiempo + "&Authorization=" + token))
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();

	            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
	            
	            switch (response.statusCode()) {
	                case 200 -> {} // Reto creado correctamente
	                case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
	                case 500 -> throw new RuntimeException("Internal server error");
	                default -> throw new RuntimeException("Fallo al crear el reto con el codigo de estatus: " + response.statusCode());
            }
		}catch(IOException | InterruptedException e) {
			throw new RuntimeException("Error creando el reto.", e);
		}		
	}

	@Override
	public List<Reto> consultarReto(String deporte, String fecha, String token) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/acciones/retos?deporte=" + deporte + "&fecha=" + 
	                		fecha + "&Authorization=" + token))
	                .header("Content-Type", "application/json")
	                .GET()
	                .build();

	            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	            
	            return switch (response.statusCode()) {
	                case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Reto.class));
	                case 204 -> throw new RuntimeException("No Content: No hay retos aceptados");
	                case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
	                case 500 -> throw new RuntimeException("Internal server error");
	                default -> throw new RuntimeException("Fallo al aceptar el reto con el codigo de estatus: " + response.statusCode());
            };
		}catch(IOException | InterruptedException e) {
			throw new RuntimeException("Error aceptando el reto.", e);
		}
	}
	
	@Override
	public void aceptarReto(Long retoId, String token) {
	    try {
	        HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(BASE_URL + "/acciones/retos/" + retoId + "/aceptar?Authorization=" + token))
	            .header("Content-Type", "application/json")
	            .POST(HttpRequest.BodyPublishers.noBody())
	            .build();

	        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

	        switch (response.statusCode()) {
	            case 200 -> {} // Reto aceptado correctamente
	            case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
	            case 404 -> throw new RuntimeException("Not Found: Reto no encontrado");
	            case 500 -> throw new RuntimeException("Internal server error");
	            default -> throw new RuntimeException("Fallo al aceptar el reto con el código de estado: " + response.statusCode());
	        }
	    } catch (IOException | InterruptedException e) {
	        throw new RuntimeException("Error aceptando el reto.", e);
	    }
	}

	@Override
	public List<Reto> consultarRetosAceptados(String token) {
	    try {
	        HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(BASE_URL + "/acciones/retos/aceptados?Authorization=" + token))
	            .header("Content-Type", "application/json")
	            .GET()
	            .build();

	        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

	        return switch (response.statusCode()) {
	            case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Reto.class));
	            case 204 -> throw new RuntimeException("No Content: No hay retos aceptados");
	            case 401 -> throw new RuntimeException("Unauthorized: Usuario no autenticado");
	            case 500 -> throw new RuntimeException("Internal server error");
	            default -> throw new RuntimeException("Fallo al consultar los retos aceptados con el código de estado: " + response.statusCode());
	        };
	    } catch (IOException | InterruptedException e) {
	        throw new RuntimeException("Error consultando los retos aceptados.", e);
	    }
	}

	@Override
	public void registro(String nombre, String email, Login tipo, String fecha_nac, float peso, int altura,
			int frec_car_max, int frec_car_rep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logIn(String email, String contrasenia) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generarToken(Usuario u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void LogOut(Usuario u) {
		// TODO Auto-generated method stub
		
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
	                case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Sesion.class));
	                case 204 -> throw new RuntimeException("No Content: No existen sesiones");
	                case 500 -> throw new RuntimeException("Internal server error");
	                default -> throw new RuntimeException("Fallo al acceder a las sesiones recientes: " + response.statusCode());
            };
		}catch(IOException | InterruptedException e) {
			throw new RuntimeException("Error accediendo a las sesiones recientes.", e);
		}
	}

	@Override
	public List<Sesion> getSesionesPorFecha(String startDate, String endDate) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/sesiones/sesionesporfecha?startDate=" + startDate + "&endDate=" + 
	                		endDate))
	                .header("Content-Type", "application/json")
	                .GET()
	                .build();

	            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
	            
	            return switch (response.statusCode()) {
	                case 200 -> objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Sesion.class));
	                case 204 -> throw new RuntimeException("No Content: No existen sesiones");
	                case 400 -> throw new RuntimeException("Formato de fecha invalido");
	                case 500 -> throw new RuntimeException("Internal server error");
	                default -> throw new RuntimeException("Fallo al acceder a las sesiones recientes: " + response.statusCode());
            };
		}catch(IOException | InterruptedException e) {
			throw new RuntimeException("Error accediendo a las sesiones recientes.", e);
		}
	}

	@Override
	public void crearSesion(Long sesionId, String titulo, String deporte, double distancia, LocalDate fechaInicio, int duracion) {
		try {
			HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(BASE_URL + "/sesiones/crear/?sesionId=" + sesionId + "&titulo=" + 
	                		titulo + "&deporte=" + deporte + "&distancia=" +distancia + "&fechaInicio=" +
	                		fechaInicio + "&duracion=" +duracion))
	                .header("Content-Type", "application/json")
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();

	            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
	            
	            switch (response.statusCode()) {
	            case 200 -> {} // Sesión creada exitosamente
	            case 400 -> throw new RuntimeException("Bad Request: Datos inválidos o faltantes en la solicitud");
	            case 500 -> throw new RuntimeException("Internal Server Error: Error en el servidor al crear la sesión");
	            default -> throw new RuntimeException("Error inesperado con código de estado: " + response.statusCode());
	        }
	    } catch (IOException | InterruptedException e) {
	        throw new RuntimeException("Error al intentar crear la sesión", e);
	    }	
	}

}